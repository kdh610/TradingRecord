package com.tradingRecord.tradingRecord.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TradeRequest(
        String stkNm,
        String tradeType,
        Boolean isStupid,
        List<UUID> orderLogIds,
        String comment,
        String review,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        @JsonProperty("trade_day") LocalDate tradeDay //기준일자 YYYYMMDD
) {
}
