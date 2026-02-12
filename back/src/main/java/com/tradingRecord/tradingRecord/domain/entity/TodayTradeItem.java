package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomTradeItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Double selAvgPric; //매도평균가
    private Double plAmt; //손익금액
    private Double prftRt; //수익률
    private LocalDate tradeDay;

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
                .selAvgPric(Double.valueOf(item.selAvgPrice()))
                .plAmt(Double.valueOf(item.plAmt()))
                .prftRt(Double.valueOf(item.profitRate()))
                .build();
    }

}
