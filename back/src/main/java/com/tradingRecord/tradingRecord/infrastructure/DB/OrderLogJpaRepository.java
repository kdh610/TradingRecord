package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderLogJpaRepository extends JpaRepository<OrderLog, UUID> {
}
