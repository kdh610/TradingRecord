package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KiwoomOrderLogResponse(
        @JsonProperty("sell_grntl_engg_amt")String sellGmtlEnggAmt,
        @JsonProperty("buy_engg_amt")String buyEnggAmt,
        @JsonProperty("engg_amt")String enggAmt,
        @JsonProperty("acnt_ord_cntr_prst_array")List<KiwoomOrderLogItem> orderLogItems
) {
}
