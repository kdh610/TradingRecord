package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.SearchTradeResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.Trade;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeRepository;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import com.tradingRecord.tradingRecord.infrastructure.exception.BaseException;
import com.tradingRecord.tradingRecord.presentation.dto.SearchOrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.SearchTradeRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .orElseThrow(()-> new BaseException(Code.TRADE_DIARY_NOT_FOUND));

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

    public Page<SearchTradeResponse> searchTrade(SearchTradeRequest request, Pageable pageable){
        Page<Trade> trades = tradeRepository.searchTrade(request, pageable);
        return trades.map(SearchTradeResponse::from);
    }


    @Transactional
    public void deleteTrade(String id){
        tradeRepository.findById(UUID.fromString(id)).orElseThrow(()->new BaseException(Code.TRADE_NOT_FOUND));

        orderLogRepository.detachOrderLogsByTradeId(UUID.fromString(id));

        tradeRepository.deleteTrade(UUID.fromString(id));


    }



}
