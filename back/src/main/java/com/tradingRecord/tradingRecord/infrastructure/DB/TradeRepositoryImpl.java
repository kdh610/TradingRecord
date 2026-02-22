package com.tradingRecord.tradingRecord.infrastructure.DB;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tradingRecord.tradingRecord.domain.entity.QTrade;
import com.tradingRecord.tradingRecord.domain.entity.Trade;
import com.tradingRecord.tradingRecord.domain.repository.TradeRepository;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import com.tradingRecord.tradingRecord.infrastructure.exception.BaseException;
import com.tradingRecord.tradingRecord.presentation.dto.SearchTradeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TradeRepositoryImpl implements TradeRepository {

    private final TradeJpaRepository tradeJpaRepository;
    private final JPAQueryFactory queryFactory;
    QTrade trade = QTrade.trade;

    @Override
    public Trade save(Trade trade) {
        return tradeJpaRepository.save(trade);
    }

    @Override
    public Trade findById(UUID id) {
        return tradeJpaRepository.findById(id).orElseThrow(()->new BaseException(Code.TRADE_NOT_FOUND));
    }

    @Override
    public Page<Trade> searchTrade(SearchTradeRequest request, Pageable pageable) {

        List<Trade> content = queryFactory.selectFrom(trade)
                .where(
                        stkNmContains(request.stkNm()),
                        tradingTypeEq(request.tradingType()),
                        isWinEq(request.isWin()),
                        isStupidEq(request.isStupid()),
                        tradeDayBetween(request.start(), request.end())
                )
                .orderBy(trade.tradeDay.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(trade.count())
                .from(trade)
                .where(
                        stkNmContains(request.stkNm()),
                        tradingTypeEq(request.tradingType()),
                        isWinEq(request.isWin()),
                        isStupidEq(request.isStupid()),
                        tradeDayBetween(request.start(), request.end())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression stkNmContains(String stkNm){
        return hasText(stkNm) ? trade.stkNm.contains(stkNm) : null;
    }

    private BooleanExpression tradingTypeEq(String tradingType){
        return hasText(tradingType) ? trade.tradingType.eq(tradingType) : null;
    }

    private BooleanExpression isWinEq(Boolean isWin) {
        return isWin != null ? trade.winLose.eq(isWin) : null;
    }

    private BooleanExpression isStupidEq(Boolean isStupid) {
        return isStupid != null ? trade.stupid.eq(isStupid) : null;
    }

    private BooleanExpression tradeDayBetween(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return trade.tradeDay.between(start, end);
        }
        if (start != null) {
            return trade.tradeDay.eq(start);
        }
        return null;
    }

    private boolean hasText(String str) {
        return str != null && !str.isBlank();
    }
}
