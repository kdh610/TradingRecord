package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.StockApiClient;
import com.tradingRecord.tradingRecord.application.dto.tradeLog.KiwoomTradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.dto.tradeLog.TradeLogRequest;
import com.tradingRecord.tradingRecord.application.service.StockApiService;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class KiwoomApiController {

    private final StockApiService stockApiService;

    @PostMapping("/tradeDiary")
    public ResponseEntity<String> getDailyTradeDiary(@RequestBody TradeLogRequest request){
        stockApiService.saveTradeDiary(request);
        return ResponseEntity.ok("save");
    }

}
