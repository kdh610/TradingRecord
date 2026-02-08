package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeRecordService {

    private final TradeDiaryRepository tradeDiaryRepository;

    public TradeDiaryResponse getTradeDiary(LocalDate date){
        log.info("date {}", date);
        TradeDiary tradeDiary = tradeDiaryRepository.findByTradeDay(date);
        log.info("tradeDiary {}", tradeDiary);
        return TradeDiaryResponse.from(tradeDiary);
    }


}
