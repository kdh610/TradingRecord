package com.tradingRecord.tradingRecord.application.dto.tradeLog;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TradeLogRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        LocalDate base_dt,   // 기준일자
        String ottks_tp,  // 단주구분
        String ch_crd_tp  // 현금신용구분
) {

}
