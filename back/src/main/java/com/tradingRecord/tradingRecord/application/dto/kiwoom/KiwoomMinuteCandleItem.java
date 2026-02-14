package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KiwoomMinuteCandleItem(
        @JsonProperty("cur_prc") String curPrc,       // 현재가
        @JsonProperty("trde_qty") String trdeQty,     // 거래량
        @JsonProperty("cntr_tm") String cntrTm,       // 체결시간 (yyyyMMddHHmmss)
        @JsonProperty("open_pric") String openPric,   // 시가
        @JsonProperty("high_pric") String highPric,   // 고가
        @JsonProperty("low_pric") String lowPric,     // 저가
        @JsonProperty("acc_trde_qty") String accTrdeQty, // 누적거래량
        @JsonProperty("pred_pre") String predPre,     // 전일대비
        @JsonProperty("pred_pre_sig") String predPreSig // 전일대비기호
) {
}
