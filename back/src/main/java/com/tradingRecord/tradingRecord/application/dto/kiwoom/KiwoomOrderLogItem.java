package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record KiwoomOrderLogItem(
        @JsonProperty("stk_bond_tp")String stkBondTp,
        @JsonProperty("ord_no")String ordNo,
        @JsonProperty("stk_cd")String stkCd,
        @JsonProperty("trde_tp")String trdeTp,
        @JsonProperty("io_tp_nm")String ioTpNm,
        @JsonProperty("ord_qty")String ordQty,
        @JsonProperty("ord_uv")String ordUv,
        @JsonProperty("cnfm_qty")String cnfmQty,
        @JsonProperty("rsrv_oppo")String rsrvOppo,
        @JsonProperty("cntr_no")String cntrNo,
        @JsonProperty("acpt_tp")String acptTp,
        @JsonProperty("orig_ord_no")String origOrdNo,
        @JsonProperty("stk_nm")String stkNm,
        @JsonProperty("setl_tp")String setlTp,
        @JsonProperty("crd_deal_tp")String crdDealTp,
        @JsonProperty("cntr_qty")String cntrQty,
        @JsonProperty("cntr_uv")String cntrUv,
        @JsonProperty("comm_ord_tp")String commOrdTp,
        @JsonProperty("mdfy_cncl_tp")String mdfyCnclTp,
        @JsonProperty("cntr_tm")String cntrTm,
        @JsonProperty("dmst_stex_tp")String dmstStexTp,
        @JsonProperty("cond_uv")String condUv
) {
}
