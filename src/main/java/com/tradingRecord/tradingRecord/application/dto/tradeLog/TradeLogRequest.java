package com.tradingRecord.tradingRecord.application.dto.tradeLog;

public record TradeLogRequest(
        String base_dt,   // 기준일자
        String ottks_tp,  // 단주구분
        String ch_crd_tp  // 현금신용구분
) {

}
