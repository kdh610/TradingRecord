package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.QOrderLog;
import com.tradingRecord.tradingRecord.domain.repository.OrderLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderLogRepositoryImpl implements OrderLogRepository {
    private final OrderLogJpaRepository orderLogJpaRepository;
    private final JPAQueryFactory queryFactory;
    QOrderLog orderLog = QOrderLog.orderLog;

    @Override
    public List<OrderLog> saveAll(List<OrderLog> orderLogs) {
        return orderLogJpaRepository.saveAll(orderLogs);
    }

    @Override
    public List<OrderLog> search(String stkNm, LocalDate start, LocalDate end) {
        return queryFactory
                .selectFrom(orderLog)
                .where(
                        orderLog.stkNm.eq(stkNm),
                        betweenDates(start, end)
                ).orderBy(
                        orderLog.tradeDay.asc(),
                        orderLog.cntrTm.asc()
                )
                .fetch();
    }

    @Override
    public List<OrderLog> findAllById(List<UUID> logIds) {
        return orderLogJpaRepository.findAllById(logIds);
    }

    @Override
    public void detachOrderLogsByTradeId(UUID tradeId) {
        orderLogJpaRepository.detachOrderLogsByTradeId(tradeId);
    }

    private BooleanExpression betweenDates(LocalDate start, LocalDate end) {
        if(start==null) return null;

        if(end==null || end.equals(start)){
            return orderLog.tradeDay.eq(start);
        }

        return orderLog.tradeDay.between(start,end);
    }
}
