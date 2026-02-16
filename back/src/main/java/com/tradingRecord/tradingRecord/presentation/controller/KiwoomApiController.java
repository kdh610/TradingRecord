package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomMinuteCandleResponse;
import com.tradingRecord.tradingRecord.infrastructure.common.ApiResponse;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import com.tradingRecord.tradingRecord.presentation.dto.MinuteCandleRequest;
import com.tradingRecord.tradingRecord.presentation.dto.OrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeLogRequest;
import com.tradingRecord.tradingRecord.application.service.StockApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class KiwoomApiController {

    private final StockApiService stockApiService;

    @PostMapping("/stock-company/trade-diaries")
    public ResponseEntity<ApiResponse<String>> saveDailyTradeDiary(@RequestBody TradeLogRequest request){
        stockApiService.saveTradeDiary(request);
        return ResponseEntity.ok(ApiResponse.success(Code.SUCCESS.getMessage()));
    }

    @PostMapping("/stock-company/order-logs")
    public ResponseEntity<ApiResponse<String>> saveOrderLog(@RequestBody OrderLogRequest request){
        stockApiService.saveOrderLog(request);
        return ResponseEntity.ok(ApiResponse.success(Code.SUCCESS.getMessage()));
    }

    @PostMapping("/stock-company/minute-candles")
    public ResponseEntity<ApiResponse<KiwoomMinuteCandleResponse>> getMinuteCandle(@RequestBody MinuteCandleRequest request){
        KiwoomMinuteCandleResponse minuteCandle = stockApiService.getMinuteCandle(request);
        return ResponseEntity.ok(ApiResponse.success(minuteCandle));
    }


}
