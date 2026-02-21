package com.tradingRecord.tradingRecord.application.dto;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import com.tradingRecord.tradingRecord.domain.entity.Trade;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record SearchTradeResponse(
        UUID id,
        LocalDate tradeDay,
        String stkNm,
        String tradingType,
        Double prftRt,
        Double plAmt,
        Boolean winLose,
        Boolean stupid,
        String review,
        String comment
) {

    public static SearchTradeResponse from(Trade trade){
        return SearchTradeResponse.builder()
                .id(trade.getId())
                .tradeDay(trade.getTradeDay())
                .stkNm(trade.getStkNm())
                .tradingType(trade.getTradingType())
                .prftRt(trade.getPrftRt())
                .plAmt(trade.getPlAmt())
                .winLose(trade.getWinLose())
                .stupid(trade.getStupid())
                .review(trade.getReview())
                .comment(trade.getComment())
                .build();
    }


}
