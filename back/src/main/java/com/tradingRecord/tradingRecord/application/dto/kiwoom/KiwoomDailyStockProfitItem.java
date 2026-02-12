package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KiwoomDailyStockProfitItem(
        @JsonProperty("stk_nm") String stockName,          // 종목명
        @JsonProperty("cntr_qty") String quantity,            // 체결 수량
        @JsonProperty("buy_uv") String buyUnitPrice,    // 매수 단가
        @JsonProperty("cntr_pric") String sellPrice,    // 체결 가격 (매도 단가)
        @JsonProperty("tdy_sel_pl") String netProfit,   // 당일 매도 손익
        @JsonProperty("pl_rt") String profitRate,       // 수익률
        @JsonProperty("stk_cd") String stockCode,           // 종목 코드
        @JsonProperty("tdy_trde_cmsn") long commission,     // 당일 매매 수수료
        @JsonProperty("tdy_trde_tax") long tax,             // 당일 매매 제세금
        @JsonProperty("crd_tp") String creditType,          // 신용 구분 (현금/신용)
        @JsonProperty("loan_dt") String loanDate            // 대출 일자

) {

}
