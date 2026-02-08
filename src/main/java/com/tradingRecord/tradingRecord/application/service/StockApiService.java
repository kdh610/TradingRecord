package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.OrderLogRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TradeLogRequest;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;

public interface StockApiService {
    void saveTradeDiary(TradeLogRequest request);
    void saveOrderLog(OrderLogRequest orderLogRequest);
}
