package com.tradingRecord.tradingRecord.presentation.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SearchTradeRequest(
        String stkNm,
        String tradingType,
        Boolean isWin,
        Boolean isStupid,
        @DateTimeFormat(pattern = "yyyyMMdd")
        LocalDate start,
        @DateTimeFormat(pattern = "yyyyMMdd")
        LocalDate end

) {
}
