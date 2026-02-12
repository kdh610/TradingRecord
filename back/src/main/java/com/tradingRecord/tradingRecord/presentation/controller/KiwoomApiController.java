package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.StockCompanyApiClient;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.DailyRealProfitRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.DailyStockProfitRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.OrderLogRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TradeLogRequest;
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

    @PostMapping("/tradeDiary")
    public ResponseEntity<String> saveDailyTradeDiary(@RequestBody TradeLogRequest request){
        stockApiService.saveTradeDiary(request);
        return ResponseEntity.ok("save");
    }

    @PostMapping("/orderLog")
    public ResponseEntity<String> saveOrderLog(@RequestBody OrderLogRequest request){
        stockApiService.saveOrderLog(request);
        return ResponseEntity.ok("save");
    }


}
