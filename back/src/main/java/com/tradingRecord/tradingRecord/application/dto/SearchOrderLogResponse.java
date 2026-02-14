package com.tradingRecord.tradingRecord.application.dto;

import com.tradingRecord.tradingRecord.domain.entity.OrderLog;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record SearchOrderLogResponse(
        UUID id,
        LocalDate tradeDay,
        String cntrTm,
        String stkNm,
        String stkCd,
        String ioTpNm,
        Double ordQty,
        Double ordUv,
        Double cntrQty,
        Double cntrUv,
        Boolean isUsed
) {

    public static SearchOrderLogResponse from(OrderLog orderLog){
        return SearchOrderLogResponse.builder()
                .id(orderLog.getId())
                .tradeDay(orderLog.getTradeDay())
                .stkCd(orderLog.getStkCd())
                .cntrTm(orderLog.getCntrTm())
                .stkNm(orderLog.getStkNm())
                .ioTpNm(orderLog.getIoTpNm())
                .ordQty(orderLog.getOrdQty())
                .ordUv(orderLog.getOrdUv())
                .cntrQty(orderLog.getCntrQty())
                .cntrUv(orderLog.getCntrUv())
                .isUsed(orderLog.getIsUsed())
                .build();
    }


}
