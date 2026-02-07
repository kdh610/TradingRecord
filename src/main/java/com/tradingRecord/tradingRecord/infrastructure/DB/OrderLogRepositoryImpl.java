package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderLogRepositoryImpl implements OrderLogRepository {
    private final OrderLogJpaRepository orderLogJpaRepository;

    @Override
    public List<OrderLog> saveAll(List<OrderLog> orderLogs) {
        return orderLogJpaRepository.saveAll(orderLogs);
    }
}
