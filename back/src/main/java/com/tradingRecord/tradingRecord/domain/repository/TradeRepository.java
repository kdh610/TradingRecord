package com.tradingRecord.tradingRecord.domain.repository;

import com.tradingRecord.tradingRecord.domain.entity.Trade;
import com.tradingRecord.tradingRecord.presentation.dto.SearchTradeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TradeRepository {
    Trade save(Trade trade);
    Optional<Trade> findById(UUID id);
    Page<Trade> searchTrade(SearchTradeRequest request, Pageable pageable);
    void deleteTrade(UUID id);
}
