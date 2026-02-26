package com.tradingRecord.tradingRecord.application.stockApi;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.*;
import com.tradingRecord.tradingRecord.presentation.dto.MinuteCandleRequest;
import com.tradingRecord.tradingRecord.presentation.dto.OrderLogRequest;
import com.tradingRecord.tradingRecord.presentation.dto.TradeLogRequest;

import java.util.List;
import java.util.Optional;

public interface StockCompanyApiClient {
    Optional<KiwoomTradeDiaryResponse> requestTradeLog(TradeLogRequest request);
    Optional<List<KiwoomOrderLogItem>> requestOrderLog(OrderLogRequest request);
    Optional<KiwoomDailyRealProfitResponse> requestDailyRealProfit(DailyRealProfitRequest request);
    Optional<KiwoomDailyStockProfitResponse> requestDailyStockProfit(DailyStockProfitRequest request);
    Optional<KiwoomMinuteCandleResponse> requestMinuteCandle(MinuteCandleRequest request);


}
