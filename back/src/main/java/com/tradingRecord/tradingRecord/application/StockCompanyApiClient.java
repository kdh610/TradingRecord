package com.tradingRecord.tradingRecord.application;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;

import java.util.List;
import java.util.Optional;

public interface StockCompanyApiClient {
    Optional<KiwoomTradeDiaryResponse> requestTradeLog(TradeLogRequest request);
    Optional<List<KiwoomOrderLogItem>> requestOrderLog(OrderLogRequest request);
    Optional<KiwoomDailyRealProfitResponse> requestDailyRealProfit(DailyRealProfitRequest request);
    Optional<KiwoomDailyStockProfitResponse> requestDailyStockProfit(DailyStockProfitRequest request);
}
