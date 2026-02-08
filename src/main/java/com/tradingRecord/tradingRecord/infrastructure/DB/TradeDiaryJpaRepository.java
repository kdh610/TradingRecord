package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TradeDiaryJpaRepository extends JpaRepository<TradeDiary, UUID> {
    TradeDiary findByTradeDay(LocalDate date);
}
