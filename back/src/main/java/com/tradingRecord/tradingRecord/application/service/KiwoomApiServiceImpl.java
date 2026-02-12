package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.RateLimiterManager;
import com.tradingRecord.tradingRecord.application.StockCompanyApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.TodayTradeItem;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KiwoomApiServiceImpl implements StockApiService{

    private final StockCompanyApiClient stockCompanyApiClient;
    private final TradeDiaryRepository tradeDiaryRepository;
    private final OrderLogRepository orderLogRepository;
    private final RateLimiterManager rateLimiterManager;

    @Override
    @Transactional
    public void saveTradeDiary(TradeLogRequest request) {
        /**
         * 당일 매매한 종목들에 대한 정보를 가져오기 위한 요청
         */
        KiwoomTradeDiaryResponse tradeDiaryResponse = stockCompanyApiClient
                .requestTradeLog(request)
                .orElseThrow(() -> new RuntimeException("해당 날짜 일지가 없습니다."));

        /**
         * 당일 매매일지 요청만으로는 오버나잇한 종목들에 대한 수익금, 수익률이 0으로 나와서
         * 당일 매매한 총 수익금, 수익률 등을 가져오기 위한 일자별실현손익 요청
         */
        KiwoomDailyRealProfitResponse dailyRealProfit = stockCompanyApiClient
                .requestDailyRealProfit(DailyRealProfitRequest.create(request.baseDt()))
                .orElseThrow(() -> new RuntimeException("해당 날짜 실현손익이 없습니다."));
        TradeDiary tradeDiary = TradeDiary.of(request.baseDt(), dailyRealProfit);

        /**
         * 당일 매매일지 요청만으로는 오버나잇한 종목들에 대한 수익금, 수익률이 0으로 나와서
         * 당일 매매한 종목의 정확한 수익금을 가져오기 위한 요청
         */
        List<KiwoomTradeItem> kiwoomTradeItems = tradeDiaryResponse.tradeDiaryList();
        for(KiwoomTradeItem item: kiwoomTradeItems){
            String stockCode = item.stockCode();
            LocalDate date = request.baseDt();

            ConsumptionProbe probe = rateLimiterManager.probe();

            if (!probe.isConsumed()) {
                long waitTimeMs = probe.getNanosToWaitForRefill() / 1_000_000;
                log.info("레이트 리밋");
                try {
                    Thread.sleep(Math.max(waitTimeMs, 10));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("대기 중 흐름이 끊겼습니다.", e);
                }
            }
            stockCompanyApiClient.requestDailyStockProfit(DailyStockProfitRequest.create(date, stockCode))
                    .flatMap(KiwoomDailyStockProfitResponse::calculateProfit)
                    .ifPresent(tradeDiary::addTodayTradeDiary);

        }

        tradeDiaryRepository.save(tradeDiary);
    }

    @Override
    public void saveOrderLog(OrderLogRequest orderLogRequest) {
        List<KiwoomOrderLogItem> result = stockCompanyApiClient.requestOrderLog(orderLogRequest).orElseThrow(()->new RuntimeException("해당 날짜 주문체결이 없습니다."));

        List<OrderLog> orderLogs = result.stream().map(item -> OrderLog.from(orderLogRequest,item)).toList();
        orderLogRepository.saveAll(orderLogs);
    }
}
