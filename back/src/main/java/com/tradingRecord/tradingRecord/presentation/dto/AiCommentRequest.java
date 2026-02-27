package com.tradingRecord.tradingRecord.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record AiCommentRequest(
        UUID id,
        String stkNm,
        String tradeType,
        Double plAmt,
        Boolean stupid,
        String review,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        @JsonProperty("trade_day") LocalDate tradeDay //기준일자 YYYYMMDD
) {
}
