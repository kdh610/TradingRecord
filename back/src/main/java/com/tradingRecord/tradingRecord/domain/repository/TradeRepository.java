package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.Trade;

import java.util.UUID;

public interface TradeRepository {
    Trade save(Trade trade);
    Trade findById(UUID id);
}
