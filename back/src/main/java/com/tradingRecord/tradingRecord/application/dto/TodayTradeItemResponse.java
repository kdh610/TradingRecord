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
                .buyQty(item.getBuyQty())
                .selAvgPric(item.getSelAvgPric())
                .sellQty(item.getSellQty())
                .cmsnAlmTax(item.getCmsnAlmTax())
                .plAmt(item.getPlAmt())
                .sellAmt(item.getSellAmt())
                .buyAmt(item.getBuyAmt())
                .prftRt(String.valueOf(item.getPrftRt()))
                .stkCd(item.getStkCd())
                .build();
    }

}
