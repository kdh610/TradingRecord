package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.OrderLogRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TradeLogRequest;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;

public interface StockApiService {
    TradeDiary saveTradeDiary(TradeLogRequest request);
    void saveOrderLog(OrderLogRequest orderLogRequest);
}
