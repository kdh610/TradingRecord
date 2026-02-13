package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TradeDiaryRepository {
    TradeDiary save(TradeDiary tradeDiary);
    Optional<TradeDiary> findByTradeDay(LocalDate date);
}
