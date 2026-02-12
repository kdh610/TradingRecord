package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DailyStockProfitRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        @JsonProperty("strt_dt") LocalDate strtDt, //시작일자
        @JsonProperty("stk_cd") String stkCd //종목코드
) {
        public static DailyStockProfitRequest create(LocalDate strtDt, String stkCd){
                return DailyStockProfitRequest.builder().strtDt(strtDt).stkCd(stkCd).build();
        }
}
