package com.tradingRecord.tradingRecord.application.dto.tradeLog;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KiwoomTradeItem(
        @JsonProperty("stk_nm") String stockName,            // 종목명
        @JsonProperty("stk_cd") String stockCode,            // 종목코드
        @JsonProperty("buy_avg_pric") String buyAvgPrice,    // 매수평균가
        @JsonProperty("buy_qty") String buyQty,              // 매수수량
        @JsonProperty("sel_avg_pric") String selAvgPrice,    // 매도평균가
        @JsonProperty("sell_qty") String sellQty,            // 매도수량
        @JsonProperty("cmsn_alm_tax") String cmsnAlmTax,    // 제비용 (수수료+세금)
        @JsonProperty("pl_amt") String plAmt,                // 손익금액
        @JsonProperty("sell_amt") String sellAmt,            // 매도금액
        @JsonProperty("buy_amt") String buyAmt,              // 매수금액
        @JsonProperty("prft_rt") String profitRate           // 수익률
) {

}
