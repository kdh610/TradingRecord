package com.tradingRecord.tradingRecord.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record TradeLogRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        @JsonProperty("base_dt") LocalDate baseDt,// 기준일자
        @JsonProperty("ottks_tp") String ottksTp, // 단주구분
        @JsonProperty("ch_crd_tp") String chCrdTp// 현금신용구분
) {

}
