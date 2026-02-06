package com.tradingRecord.tradingRecord.presentation.controller;

import com.tradingRecord.tradingRecord.application.StockApiClient;
import com.tradingRecord.tradingRecord.application.dto.tradeLog.KiwoomTradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.dto.tradeLog.TradeLogRequest;
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

    private final StockApiClient stockApiClient;

    @PostMapping("/test")
    public ResponseEntity<KiwoomTradeDiaryResponse> getDailyTradeDiary(@RequestBody TradeLogRequest request){
        KiwoomTradeDiaryResponse response = stockApiClient.requestTradeLog(request).orElseThrow(() -> new RuntimeException("일지가 없습니다."));

        return ResponseEntity.ok(response);

    }

}
