package com.tradingRecord.tradingRecord.application.dto;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record TradeDiaryResponse(
        UUID id,
        LocalDate tradeDay,
        Double totSellAmt,
        Double totBuyAmt,
        Double rlztPl,
        Double totPrftRt,
        List<TodayTradeItemResponse> todayTradeItemList,
        String marketTrend
) {

    public static TradeDiaryResponse from(TradeDiary tradeDiary){
        List<TodayTradeItemResponse> todayTradeItems = tradeDiary.getTodayTradeItemList().stream()
                .map(TodayTradeItemResponse::from).toList();


        return TradeDiaryResponse.builder()
                .id(tradeDiary.getId())
                .tradeDay(tradeDiary.getTradeDay())
                .totSellAmt(tradeDiary.getTotSellAmt())
                .totBuyAmt(tradeDiary.getTotBuyAmt())
                .rlztPl(tradeDiary.getRlztPl())
                .totPrftRt(tradeDiary.getTotPrftRt())
                .todayTradeItemList(todayTradeItems)
                .marketTrend(tradeDiary.getMarketTrend())
                .build();
    }
}
