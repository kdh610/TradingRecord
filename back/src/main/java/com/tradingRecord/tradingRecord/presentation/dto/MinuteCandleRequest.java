package com.tradingRecord.tradingRecord.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MinuteCandleRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
		@JsonProperty("base_dt")LocalDate baseDt, //기준일자 YYYYMMDD
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
		@JsonProperty("start")LocalDate  start, //시작일자 YYYYMMDD 시작일자(start) ~ 기준일자(baseDt)
		@JsonProperty("upd_stkpc_tp")String updStkpcTp, //수정주가구분 0 or 1
		@JsonProperty("tic_scope")String ticScope, //1:1분, 3:3분, 5:5분, 10:10분, 15:15분, 30:30분, 45:45분, 60:60분
		@JsonProperty("stk_cd")String stkCd // 거래소별 종목코드 (KRX:039490,NXT:039490_NX,SOR:039490_AL)

) {
}
