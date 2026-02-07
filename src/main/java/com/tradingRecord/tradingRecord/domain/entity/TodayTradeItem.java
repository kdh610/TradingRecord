package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomTradeItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "today_trade_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayTradeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String stkNm;
    private Double buyAvgPric;
    private Double buyQty;
    private Double selAvgPric;
    private Double sellQty;
    private Double cmsnAlmTax;
    private Double plAmt;
    private Double sellAmt;
    private Double buyAmt;
    private String prftRt;
    private String stkCd;

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
                .prftRt(item.profitRate())
                .stkCd(item.stockCode())
                .build();
    }

}
