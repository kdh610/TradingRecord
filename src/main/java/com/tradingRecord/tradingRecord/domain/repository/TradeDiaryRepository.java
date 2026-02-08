package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;

import java.time.LocalDate;
import java.util.List;

public interface TradeDiaryRepository {
    TradeDiary save(TradeDiary tradeDiary);
    TradeDiary findByTradeDay(LocalDate date);
}
