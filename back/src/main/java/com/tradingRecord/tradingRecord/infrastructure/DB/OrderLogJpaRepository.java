package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrderLogJpaRepository extends JpaRepository<OrderLog, UUID> {
    @Modifying(clearAutomatically = true) // 벌크 연산 후 영속성 컨텍스트 초기화
    @Query("UPDATE OrderLog o SET o.isUsed = false, o.trade = null WHERE o.trade.id = :tradeId")
    void detachOrderLogsByTradeId(@Param("tradeId") UUID tradeId);
}
