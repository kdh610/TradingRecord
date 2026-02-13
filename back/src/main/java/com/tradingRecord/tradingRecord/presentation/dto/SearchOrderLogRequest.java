package com.tradingRecord.tradingRecord.presentation.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SearchOrderLogRequest(
        String stkNm,
        @DateTimeFormat(pattern = "yyyyMMdd")
        LocalDate start,
        @DateTimeFormat(pattern = "yyyyMMdd")
        LocalDate end

) {
}
