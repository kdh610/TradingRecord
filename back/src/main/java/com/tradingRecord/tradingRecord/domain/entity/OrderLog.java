package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomOrderLogItem;
import com.tradingRecord.tradingRecord.presentation.dto.OrderLogRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "order_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "trade_day")
    private LocalDate tradeDay;

    private String stkBondTp; //주식채권구분
    private String ordNo; //주문번호
    private String stkCd; //종목번호
    private String trdeTp; //매매구분
    private String ioTpNm; //주문유형구분 : 현금매도, 현금매수
    private Double ordQty; //주문수량
    private Double ordUv; //주문단가
    private String cnfmQty; //확인수량
    private String rsrvOppo; //예약/반대
    private String cntrNo; //체결번호
    private String acptTp; //접수구분
    private String origOrdNo; //원주문번호
    private String stkNm; //종목명
    private String setlTp; //결제구분
    private String crdDealTp; //신용거래구분
    private Double cntrQty; //체결수량
    private Double cntrUv; //체결단가
    private String commOrdTp; //통신구분
    private String mdfyCnclTp; //정정/취소구분
    private String cntrTm; //체결시간
    private String dmstStexTp; //국내거래소구분
    private String condUv; //스톱가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id")
    private Trade trade;

    private static final double COMMISSION_RATE = 0.00015; // 수수료 0.015%
    private static final double TAX_RATE = 0.0018; //세금


    public Boolean isBuyOrder(){
        return this.ioTpNm.contains("매수");
    }
    public Boolean isSellOrder(){
        return this.ioTpNm.contains("매도");
    }

    public double getNewAvgPrice(double curAvgPrice, double curTotQty){
        return  ((curAvgPrice * curTotQty) + (this.getBuyAmount())) / (curTotQty + this.cntrQty);
    }

    public double getBuyAmount(){
        return this.getAmount() * (1 + COMMISSION_RATE);
    }

    public double plusTotQty(double totQty){
        return totQty + this.cntrQty;
    }

    public double minusTotQty(double totQty){
        return totQty - this.cntrQty;
    }

    public double calProfitFromThisSale(double avgPrice){
        double sellMoney = this.getAmount() - (this.getAmount() * COMMISSION_RATE) - (this.getAmount() * TAX_RATE);
        return sellMoney - (avgPrice * this.cntrQty);
    }

    private double getAmount(){
        return this.cntrQty * this.cntrUv; //체결금액
    }


    public void setTrade(Trade trade){
        this.trade = trade;
    }

    public static OrderLog from(OrderLogRequest request, KiwoomOrderLogItem response){
        return OrderLog.builder()
                .tradeDay(request.ordDt())
                .stkBondTp(response.stkBondTp())
                .ordNo(response.ordNo())
                .stkCd(response.stkCd())
                .trdeTp(response.trdeTp())
                .ioTpNm(response.ioTpNm())
                .ordQty(parseSafeDouble(response.ordQty()))
                .ordUv(parseSafeDouble(response.ordUv()))
                .cnfmQty(response.cnfmQty())
                .rsrvOppo(response.rsrvOppo())
                .cntrNo(response.cntrNo())
                .acptTp(response.acptTp())
                .origOrdNo(response.origOrdNo())
                .stkNm(response.stkNm())
                .setlTp(response.setlTp())
                .crdDealTp(response.crdDealTp())
                .cntrQty(parseSafeDouble(response.cntrQty()))
                .cntrUv(parseSafeDouble(response.cntrUv()))
                .commOrdTp(response.commOrdTp())
                .mdfyCnclTp(response.mdfyCnclTp())
                .cntrTm(response.cntrTm())
                .dmstStexTp(response.dmstStexTp())
                .condUv(response.condUv())
                .build();
    }

    private static Double parseSafeDouble(String value) {
        if (value == null || value.isBlank()) {
            return 0.0; // 데이터가 없으면 0.0으로 기본값 설정
        }
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return 0.0; // 숫자가 아닌 값이 들어올 경우의 방어 로직
        }
    }








}
