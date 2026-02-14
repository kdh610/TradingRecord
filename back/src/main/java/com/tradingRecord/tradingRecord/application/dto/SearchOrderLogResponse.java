package com.tradingRecord.tradingRecord.application.dto;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SearchOrderLogResponse(
        LocalDate tradeDay,
        String cntrTm,
        String stkNm,
        String stkCd,
        String ioTpNm,
        Double ordQty,
        Double ordUv,
        Double cntrQty,
        Double cntrUv
) {

    public static SearchOrderLogResponse from(OrderLog orderLog){
        return SearchOrderLogResponse.builder()
                .tradeDay(orderLog.getTradeDay())
                .stkCd(orderLog.getStkCd())
                .cntrTm(orderLog.getCntrTm())
                .stkNm(orderLog.getStkNm())
                .ioTpNm(orderLog.getIoTpNm())
                .ordQty(orderLog.getOrdQty())
                .ordUv(orderLog.getOrdUv())
                .cntrQty(orderLog.getCntrQty())
                .cntrUv(orderLog.getCntrUv())
                .build();
    }


}
