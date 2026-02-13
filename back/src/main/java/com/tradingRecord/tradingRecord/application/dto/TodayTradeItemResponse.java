package com.tradingRecord.tradingRecord.application.dto;

import com.tradingRecord.tradingRecord.domain.entity.TodayTradeItem;
import lombok.Builder;

@Builder
public record TodayTradeItemResponse(
        String stkNm,
        Double buyAvgPric,
        Double buyQty,
        Double selAvgPric,
        Double sellQty,
        Double cmsnAlmTax,
        Double plAmt,
        Double sellAmt,
        Double buyAmt,
        String prftRt,
        String stkCd
) {
    public static TodayTradeItemResponse from(TodayTradeItem item){
        return TodayTradeItemResponse.builder()
                .stkNm(item.getStkNm())
                .buyAvgPric(item.getBuyAvgPric())
                .selAvgPric(item.getSelAvgPric())
                .plAmt(item.getPlAmt())
                .prftRt(String.valueOf(item.getPrftRt()))
                .build();
    }

}
