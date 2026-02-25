package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TradeJpaRepository extends JpaRepository<Trade, UUID> {
    Optional<List<TradeSummary>> findAllProjectedBy();
}
