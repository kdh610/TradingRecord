package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KiwoomDailyStockProfitResponse(
        @JsonProperty("dt_stk_div_rlzt_pl") List<KiwoomDailyStockProfitItem> dtStkDivRlztPl // 일자별 종목별 매매 리스트
) {
}
