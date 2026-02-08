package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.service.TradeRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TradingRecordController {

    private final TradeRecordService tradeRecordService;

    @GetMapping("/trade-diaries/{date}")
    public ResponseEntity<TradeDiaryResponse> getTradeDiaryByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date){
        log.info("date {}",date);
        TradeDiaryResponse tradeDiary = tradeRecordService.getTradeDiary(date);
        return ResponseEntity.ok(tradeDiary);
    }
}
