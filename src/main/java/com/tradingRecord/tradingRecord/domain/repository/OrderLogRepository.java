package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;

import java.util.List;

public interface OrderLogRepository {
    List<OrderLog> saveAll(List<OrderLog> orderLogs);
}
