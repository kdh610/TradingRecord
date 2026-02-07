package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record OrderLogRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
        LocalDate ord_dt, //주문일자 YYYYMMDD
		String stk_bond_tp, //주식채권구분 0:전체, 1:주식, 2:채권
		String mrkt_tp, //시장구분 0:전체, 1:코스피, 2:코스닥, 3:OTCBB, 4:ECN
		String sell_tp, //매도수구분 0:전체, 1:매도, 2:매수
        String qry_tp, //조회구분 0:전체, 1:체결
        String stk_cd, //'130660' 종목코드 전문 조회할 종목코드 String
        String fr_ord_no, //시작주문번호
        String dmst_stex_tp //국내거래소구분 %:(전체),KRX:한국거래소,NXT:넥스트트레이드,SOR:최선주문집행
) {
}
