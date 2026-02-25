package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.tradingRecord.tradingRecord.domain.entity.Trade;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record TradeResponse(
        UUID id,
        LocalDate tradeDay,
        String stkNm,
        String tradingType,
        Double prftRt,
        Double plAmt,
        Boolean winLose,
        Boolean stupid
) {

    public static TradeResponse from(Trade trade){
        return TradeResponse.builder()
                .id(trade.getId())
                .tradeDay(trade.getTradeDay())
                .stkNm(trade.getStkNm())
                .tradingType(trade.getTradingType())
                .prftRt(trade.getPrftRt())
                .plAmt(trade.getPlAmt())
                .winLose(trade.getWinLose())
                .stupid(trade.getStupid())
                .build();
    }


}
