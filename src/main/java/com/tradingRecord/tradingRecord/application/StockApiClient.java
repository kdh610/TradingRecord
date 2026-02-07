package com.tradingRecord.tradingRecord.application;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomOrderLogItem;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomTradeDiaryResponse;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.OrderLogRequest;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.TradeLogRequest;

import java.util.List;
import java.util.Optional;

public interface StockApiClient {
    Optional<KiwoomTradeDiaryResponse> requestTradeLog(TradeLogRequest request);
    Optional<List<KiwoomOrderLogItem>> requestOrderLog(OrderLogRequest request);

}
