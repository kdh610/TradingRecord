package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DailyRealProfitRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        @JsonProperty("strt_dt") LocalDate strtDt, // 시작자
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        @JsonProperty("end_dt") LocalDate endDt // 종료일자
) {
    public static DailyRealProfitRequest create(LocalDate date){
        return DailyRealProfitRequest.builder()
                .strtDt(date)
                .endDt(date)
                .build();
    }
}
