package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;

public interface TradeDiaryRepository {
    TradeDiary save(TradeDiary tradeDiary);
}
