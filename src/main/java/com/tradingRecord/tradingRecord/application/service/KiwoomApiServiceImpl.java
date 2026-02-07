package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.StockApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KiwoomApiServiceImpl implements StockApiService{

    private final StockApiClient stockApiClient;
    private final TradeDiaryRepository tradeDiaryRepository;
    private final OrderLogRepository orderLogRepository;

    @Override
    public TradeDiary saveTradeDiary(TradeLogRequest request) {
        KiwoomTradeDiaryResponse result = stockApiClient.requestTradeLog(request).orElseThrow(() -> new RuntimeException("해당 날짜 일지가 없습니다."));
        log.info("result {}", result);
        TradeDiary tradeDiary = TradeDiary.of(request.base_dt(), result);
        return tradeDiaryRepository.save(tradeDiary);
    }

    @Override
    public void saveOrderLog(OrderLogRequest orderLogRequest) {
        List<KiwoomOrderLogItem> result = stockApiClient.requestOrderLog(orderLogRequest).orElseThrow(()->new RuntimeException("해당 날짜 주문체결이 없습니다."));

        List<OrderLog> orderLogs = result.stream().map(item -> OrderLog.from(orderLogRequest,item)).toList();
        orderLogRepository.saveAll(orderLogs);
    }
}
