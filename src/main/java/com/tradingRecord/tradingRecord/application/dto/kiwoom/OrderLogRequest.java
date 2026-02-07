package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record OrderLogRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
		@JsonProperty("ord_dt")LocalDate ordDt, //주문일자 YYYYMMDD
		@JsonProperty("stk_bond_tp")String stkBondTp, //주식채권구분 0:전체, 1:주식, 2:채권
		@JsonProperty("mrkt_tp")String mrktTp, //시장구분 0:전체, 1:코스피, 2:코스닥, 3:OTCBB, 4:ECN
		@JsonProperty("sell_tp")String sellTp, //매도수구분 0:전체, 1:매도, 2:매수
		@JsonProperty("qry_tp")String qryTp, //조회구분 0:전체, 1:체결
		@JsonProperty("stk_cd")String stkCd, //'130660' 종목코드 전문 조회할 종목코드 String
		@JsonProperty("fr_ord_no")String frOrdNo, //시작주문번호
		@JsonProperty("dmst_stex_tp")String dmstStexTp //국내거래소구분 %:(전체),KRX:한국거래소,NXT:넥스트트레이드,SOR:최선주문집행
) {
}
