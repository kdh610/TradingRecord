package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.Trade;
import com.tradingRecord.tradingRecord.domain.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TradeRepositoryImpl implements TradeRepository {

    private final TradeJpaRepository tradeJpaRepository;

    @Override
    public Trade save(Trade trade) {
        return tradeJpaRepository.save(trade);
    }

    @Override
    public Trade findById(UUID id) {
        return tradeJpaRepository.findById(id).orElseThrow(()->new RuntimeException("매매가 없음."));
    }
}
