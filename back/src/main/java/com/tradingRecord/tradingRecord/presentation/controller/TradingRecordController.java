package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.service.TradeRecordService;
import com.tradingRecord.tradingRecord.presentation.dto.SearchOrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TradingRecordController {

    private final TradeRecordService tradeRecordService;

    @GetMapping("/trade-diaries/{date}")
    public ResponseEntity<TradeDiaryResponse> getTradeDiaryByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date){
        log.info("당일 매매일지 요청 날짜 {}",date);
        TradeDiaryResponse tradeDiary = tradeRecordService.getTradeDiary(date);
        return ResponseEntity.ok(tradeDiary);
    }

    @GetMapping("/order-logs")
    public ResponseEntity<List<SearchOrderLogResponse>> searchOrderLog(
            @ModelAttribute SearchOrderLogRequest request){
        log.info("request {}", request);
        List<SearchOrderLogResponse> searchOrderLogResponses = tradeRecordService.searchOrderLog(request);
        return ResponseEntity.ok(searchOrderLogResponses);
    }

    @PostMapping("/trades")
    public ResponseEntity<String> saveTrade(@RequestBody TradeRequest requests) {
        tradeRecordService.processTradeWinRate(requests);
        return ResponseEntity.ok("성공적으로 수신되었습니다.");
    }

}
