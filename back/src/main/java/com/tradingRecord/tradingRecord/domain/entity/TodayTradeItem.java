package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomTradeItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "today_trade_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TodayTradeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String stkNm; //종목명
    private Double buyAvgPric; //매수평균가
    private Double buyQty; //매수수량
    private Double selAvgPric; //매도평균가
    private Double sellQty; //매도수량
    private Double cmsnAlmTax; //수수료제세금
    private Double plAmt; //손익금액
    private Double sellAmt; //매도금액
    private Double buyAmt; //매수금액
    private Double prftRt; //수익률
    private String stkCd; //종목코드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_diary_id")
    private TradeDiary tradeDiary;

    public TradeDiary getTradeDiary(){
        return tradeDiary;
    }
    public void setTradeDiary(TradeDiary tradeDiary){
        this.tradeDiary = tradeDiary;
    }

    public static TodayTradeItem from(KiwoomTradeItem item){
        return TodayTradeItem.builder()
                .stkNm(item.stockName())
                .buyAvgPric(Double.valueOf(item.buyAvgPrice()))
                .buyQty(Double.valueOf(item.buyQty()))
                .selAvgPric(Double.valueOf(item.selAvgPrice()))
                .sellQty(Double.valueOf(item.sellQty()))
                .cmsnAlmTax(Double.valueOf(item.cmsnAlmTax()))
                .plAmt(Double.valueOf(item.plAmt()))
                .sellAmt(Double.valueOf(item.sellAmt()))
                .buyAmt(Double.valueOf(item.buyAmt()))
                .prftRt(Double.valueOf(item.profitRate()))
                .stkCd(item.stockCode())
                .build();
    }

}
