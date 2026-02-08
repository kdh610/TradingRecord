package com.tradingRecord.tradingRecord.application.dto;

import com.tradingRecord.tradingRecord.domain.entity.TradeDiary;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TradeDiaryResponse(
        LocalDate tradeDay,
        Double totSellAmt,
        Double totBuyAmt,
        Double totCmsnTax,
        Double totExctAmt,
        Double totPlAmt,
        Double totPrftRt,
        List<TodayTradeItemResponse> todayTradeItemList
) {

    public static TradeDiaryResponse from(TradeDiary tradeDiary){
        List<TodayTradeItemResponse> todayTradeItems = tradeDiary.getTodayTradeItemList().stream()
                .map(TodayTradeItemResponse::from).toList();


        return TradeDiaryResponse.builder()
                .tradeDay(tradeDiary.getTradeDay())
                .totSellAmt(tradeDiary.getTotSellAmt())
                .totBuyAmt(tradeDiary.getTotBuyAmt())
                .totCmsnTax(tradeDiary.getTotCmsnTax())
                .totExctAmt(tradeDiary.getTotExctAmt())
                .totPlAmt(tradeDiary.getTotPlAmt())
                .totPrftRt(tradeDiary.getTotPrftRt())
                .todayTradeItemList(todayTradeItems)
                .build();
    }
}
