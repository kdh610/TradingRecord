package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderLogRepository {
    List<OrderLog> saveAll(List<OrderLog> orderLogs);
    List<OrderLog> search(String stkNm, LocalDate start, LocalDate end);
    List<OrderLog> findAllById(List<UUID> logIds);
}
