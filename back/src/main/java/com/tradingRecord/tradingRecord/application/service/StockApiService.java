package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomMinuteCandleResponse;
import com.tradingRecord.tradingRecord.presentation.dto.MinuteCandleRequest;
import com.tradingRecord.tradingRecord.presentation.dto.OrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeLogRequest;

public interface StockApiService {
    void saveTradeDiary(TradeLogRequest request);
    void saveOrderLog(OrderLogRequest orderLogRequest);
    KiwoomMinuteCandleResponse getMinuteCandle(MinuteCandleRequest request);
}
