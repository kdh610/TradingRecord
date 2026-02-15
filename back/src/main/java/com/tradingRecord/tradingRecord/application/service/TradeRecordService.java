package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.Trade;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeRepository;
import com.tradingRecord.tradingRecord.presentation.dto.SearchOrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeRecordService {

    private final TradeDiaryRepository tradeDiaryRepository;
    private final OrderLogRepository orderLogRepository;
    private final TradeRepository tradeRepository;

    public TradeDiaryResponse getTradeDiary(LocalDate date){
        TradeDiary tradeDiary = tradeDiaryRepository.findByTradeDay(date)
                .orElseThrow(()-> new RuntimeException("당일 매매일지가 없습니다."));

        return TradeDiaryResponse.from(tradeDiary);
    }

    public List<SearchOrderLogResponse> searchOrderLog(SearchOrderLogRequest request){
        List<OrderLog> orderLogs = orderLogRepository.search(request.stkNm(), request.start(), request.end());
        return orderLogs.stream().map(SearchOrderLogResponse::from).toList();
    }

    @Transactional
    public void processTradeWinRate(TradeRequest requests){
        List<OrderLog> orderLogs = orderLogRepository.findAllById(requests.orderLogIds());
        Trade newTrade = Trade.builder()
                .stkNm(requests.stkNm())
                .tradingType(requests.tradeType())
                .stupid(requests.isStupid())
                .comment(requests.comment())
                .review(requests.review())
                .tradeDay(requests.tradeDay())
                .build();

        for (OrderLog log : orderLogs) {
            newTrade.addOrderLog(log);
        }

        newTrade.calculateWinRate();
        tradeRepository.save(newTrade);

    }



}
