package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.SearchTradeResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TradeResponse;
import com.tradingRecord.tradingRecord.application.service.TradeRecordService;
import com.tradingRecord.tradingRecord.infrastructure.DB.TradeSummary;
import com.tradingRecord.tradingRecord.infrastructure.common.ApiResponse;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import com.tradingRecord.tradingRecord.presentation.dto.SearchOrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.SearchTradeRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/trades/search")
    public ResponseEntity<ApiResponse< Page<SearchTradeResponse>>> searchTrade(
            @ModelAttribute SearchTradeRequest searchTradeRequest,
            @PageableDefault(size=30, page = 0, sort = "tradeDay", direction = Sort.Direction.DESC) Pageable pageable
            ){
        Page<SearchTradeResponse> response = tradeRecordService.searchTrade(searchTradeRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/trades/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTrade(@PathVariable String id){
        tradeRecordService.deleteTrade(id);
        return ResponseEntity.ok(ApiResponse.success("delete"));
    }

    @GetMapping("/trades")
    public ResponseEntity<ApiResponse<List<TradeSummary>>> getAllTrades(){
        List<TradeSummary> allTrade = tradeRecordService.getAllTrade();
        return ResponseEntity.ok(ApiResponse.success(allTrade));
    }

}
