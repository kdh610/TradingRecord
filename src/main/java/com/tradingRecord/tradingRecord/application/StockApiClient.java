package com.tradingRecord.tradingRecord.application;

import com.tradingRecord.tradingRecord.application.dto.tradeLog.KiwoomTradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.dto.tradeLog.TradeLogRequest;

import java.util.Optional;

public interface StockApiClient {
    Optional<KiwoomTradeDiaryResponse> requestTradeLog(TradeLogRequest request);

}
