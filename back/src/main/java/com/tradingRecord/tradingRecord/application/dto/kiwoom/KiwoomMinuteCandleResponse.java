package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KiwoomMinuteCandleResponse(
        @JsonProperty("stk_cd")
        String stkCd,

        @JsonProperty("stk_min_pole_chart_qry")
        List<KiwoomMinuteCandleItem> chartItems
) {
}
