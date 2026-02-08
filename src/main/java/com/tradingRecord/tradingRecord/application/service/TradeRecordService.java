package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import com.tradingRecord.tradingRecord.presentation.dto.SearchOrderLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeRecordService {

    private final TradeDiaryRepository tradeDiaryRepository;
    private final OrderLogRepository orderLogRepository;

    public TradeDiaryResponse getTradeDiary(LocalDate date){
        log.info("date {}", date);
        TradeDiary tradeDiary = tradeDiaryRepository.findByTradeDay(date);
        log.info("tradeDiary {}", tradeDiary);
        return TradeDiaryResponse.from(tradeDiary);
    }

    public List<SearchOrderLogResponse> searchOrderLog(SearchOrderLogRequest request){
        List<OrderLog> orderLogs = orderLogRepository.search(request.stkNm(), request.start(), request.end());
        return orderLogs.stream().map(SearchOrderLogResponse::from).toList();
    }




}
