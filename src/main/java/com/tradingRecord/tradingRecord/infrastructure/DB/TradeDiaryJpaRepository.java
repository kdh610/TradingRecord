package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeDiaryJpaRepository extends JpaRepository<TradeDiary, UUID> {
}
