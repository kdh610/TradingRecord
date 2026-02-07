package com.tradingRecord.tradingRecord.application.service;

import com.tradingRecord.tradingRecord.application.dto.tradeLog.TradeLogRequest;
import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;

public interface StockApiService {
    TradeDiary saveTradeDiary(TradeLogRequest request);
}
