package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import com.tradingRecord.tradingRecord.domain.repository.TradeDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TradeDiaryRepositoryImpl implements TradeDiaryRepository {

    private final TradeDiaryJpaRepository tradeDiaryJpaRepository;

    @Override
    public TradeDiary save(TradeDiary tradeDiary) {
        return tradeDiaryJpaRepository.save(tradeDiary);
    }

    @Override
    public Optional<TradeDiary> findByTradeDay(LocalDate date) {
         return Optional.ofNullable(tradeDiaryJpaRepository.findByTradeDay(date));
    }
}
