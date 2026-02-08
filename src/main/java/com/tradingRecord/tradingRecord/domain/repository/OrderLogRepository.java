package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;

import java.time.LocalDate;
import java.util.List;

public interface OrderLogRepository {
    List<OrderLog> saveAll(List<OrderLog> orderLogs);
    List<OrderLog> search(String stkNm, LocalDate start, LocalDate end);
}
