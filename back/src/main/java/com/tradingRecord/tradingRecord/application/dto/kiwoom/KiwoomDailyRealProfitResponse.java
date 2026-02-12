package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KiwoomDailyRealProfitResponse(
        @JsonProperty("tot_sell_amt") String totalSellAmount,    // 총 매도금액
        @JsonProperty("tot_buy_amt") String totalBuyAmount,      // 총 매수금액
        @JsonProperty("rlzt_pl") String rlztPl,      // 실현손익
        @JsonProperty("trde_cmsn") String trdeCmsn,    // 매매수수료
        @JsonProperty("trde_tax") String trdeTax        // 매매세금

) {
}
