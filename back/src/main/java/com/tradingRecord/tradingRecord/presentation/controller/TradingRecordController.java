package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.service.TradeRecordService;
import com.tradingRecord.tradingRecord.infrastructure.common.ApiResponse;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
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
    public ResponseEntity<ApiResponse<TradeDiaryResponse>> getTradeDiaryByDate(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date){
        TradeDiaryResponse tradeDiary = tradeRecordService.getTradeDiary(date);
        return ResponseEntity.ok(ApiResponse.success(tradeDiary));
    }

    @GetMapping("/order-logs")
    public ResponseEntity<ApiResponse<List<SearchOrderLogResponse>>> searchOrderLog(
            @ModelAttribute SearchOrderLogRequest request){
        List<SearchOrderLogResponse> searchOrderLogResponses = tradeRecordService.searchOrderLog(request);
        return ResponseEntity.ok(ApiResponse.success(searchOrderLogResponses));
    }

    @PostMapping("/trades")
    public ResponseEntity<ApiResponse<String>> saveTrade(@RequestBody TradeRequest requests) {
        tradeRecordService.processTradeWinRate(requests);
        return ResponseEntity.ok(ApiResponse.success(Code.SUCCESS.getMessage()));
    }

}
