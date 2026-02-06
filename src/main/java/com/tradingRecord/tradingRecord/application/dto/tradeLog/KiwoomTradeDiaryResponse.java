package com.tradingRecord.tradingRecord.application.dto.tradeLog;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KiwoomTradeDiaryResponse(
        @JsonProperty("tot_sell_amt") String totalSellAmount,    // 총 매도금액
        @JsonProperty("tot_buy_amt") String totalBuyAmount,      // 총 매수금액
        @JsonProperty("tot_cmsn_tax") String totalCmsnTax,      // 총 제비용
        @JsonProperty("tot_exct_amt") String totalExctAmount,    // 총 약정금액
        @JsonProperty("tot_pl_amt") String totalPlAmount,        // 총 손익금액
        @JsonProperty("tot_prft_rt") String totalProfitRate,     // 총 수익률
        @JsonProperty("tdy_trde_diary") List<KiwoomTradeItem> tradeDiaryList, // 당일 매매 리스트
        @JsonProperty("return_code") int returnCode,
        @JsonProperty("return_msg") String returnMsg
) {
}
