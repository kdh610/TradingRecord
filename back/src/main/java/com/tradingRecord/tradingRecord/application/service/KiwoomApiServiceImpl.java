package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.StockCompanyApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KiwoomApiServiceImpl implements StockApiService{

    private final StockCompanyApiClient stockCompanyApiClient;
    private final TradeDiaryRepository tradeDiaryRepository;
    private final OrderLogRepository orderLogRepository;

    @Override
    @Transactional
    public void saveTradeDiary(TradeLogRequest request) {
        /**
         * 당일 매매한 종목들에 대한 정보를 가져오기 위한 요청
         */
        KiwoomTradeDiaryResponse tradeDiary = stockCompanyApiClient.requestTradeLog(request).orElseThrow(() -> new RuntimeException("해당 날짜 일지가 없습니다."));

        /**
         * 당일 매매일지 요청만으로는 오버나잇한 종목들에 대한 수익금, 수익률이 0으로 나와서
         * 당일 매매한 총 수익금, 수익률 등을 가져오기 위한 요청
         */
        KiwoomDailyRealProfitResponse dailyRealProfit = stockCompanyApiClient.requestDailyRealProfit(DailyRealProfitRequest.create(request.baseDt())).orElseThrow(() -> new RuntimeException("해당 날짜 실현손익이 없습니다."));

        /**
         * 당일 매매일지 요청만으로는 오버나잇한 종목들에 대한 수익금, 수익률이 0으로 나와서
         * 당일 매매한 종목의 정확한 수익금을 가져오기 위한 요청
         */
        List<KiwoomTradeItem> kiwoomTradeItems = tradeDiary.tradeDiaryList();




        log.info("result {}", tradeDiary);
//        TradeDiary newTradeDiary = TradeDiary.of(request.baseDt(), tradeDiary);
        tradeDiaryRepository.save(null);
    }

    @Override
    public void saveOrderLog(OrderLogRequest orderLogRequest) {
        List<KiwoomOrderLogItem> result = stockCompanyApiClient.requestOrderLog(orderLogRequest).orElseThrow(()->new RuntimeException("해당 날짜 주문체결이 없습니다."));

        List<OrderLog> orderLogs = result.stream().map(item -> OrderLog.from(orderLogRequest,item)).toList();
        orderLogRepository.saveAll(orderLogs);
    }
}
