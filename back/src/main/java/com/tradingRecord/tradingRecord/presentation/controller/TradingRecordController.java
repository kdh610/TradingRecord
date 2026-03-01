package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.dto.SearchOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.SearchTradeResponse;
import com.tradingRecord.tradingRecord.application.dto.TradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.service.TradeRecordService;
import com.tradingRecord.tradingRecord.infrastructure.DB.TradeSummary;
import com.tradingRecord.tradingRecord.infrastructure.LLM.GenAiClientImpl;
import com.tradingRecord.tradingRecord.infrastructure.common.ApiResponse;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import com.tradingRecord.tradingRecord.presentation.dto.*;
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
import java.util.UUID;

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

    @PostMapping("/trades-diaries/market-trend")
    public ResponseEntity<ApiResponse<String>> saveMarketTrend(@RequestBody MarketTrendRequest request){
        String response = tradeRecordService.saveMarketTrend(request.trend(), request.id());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/trades")
    public ResponseEntity<ApiResponse<String>> saveTrade(@RequestBody TradeRequest requests) {
        tradeRecordService.processTradeWinRateAndSave(requests);
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


    @PostMapping("/trades/comments")
    public ResponseEntity<ApiResponse<String>> requestLLM(@RequestBody AiCommentRequest request){
        log.info("reqeust {}",request);
        String response = tradeRecordService.saveAiComment(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/trades/comments/{date}")
    public ResponseEntity<ApiResponse<String>> saveOverallReview(
            @PathVariable("date") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date
    ){
        String response = tradeRecordService.saveOverallReview(date);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/vectorstore")
    public ResponseEntity<ApiResponse<String>> saveVectorStore(@RequestBody  String metadata){
        log.info("meta{}", metadata);
        tradeRecordService.saveMetaToVector(metadata);
        return ResponseEntity.ok(ApiResponse.success("save"));
    }
}
