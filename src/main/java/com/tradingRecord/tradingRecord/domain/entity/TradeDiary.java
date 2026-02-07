package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomTradeDiaryResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="trade_diary")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    private LocalDate tradeDay;

    private Double totSellAmt;
    private Double totBuyAmt;
    private Double totCmsnTax;
    private Double totExctAmt;
    private Double totPlAmt;
    private Double totPrftRt;

    @Builder.Default
    @OneToMany(mappedBy = "tradeDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodayTradeItem> todayTradeItemList = new ArrayList<>();

    private void addTodayTradeDiary(TodayTradeItem todayTradeItem){
        this.todayTradeItemList.add(todayTradeItem);
        if(todayTradeItem.getTradeDiary() != this){
            todayTradeItem.setTradeDiary(this);
        }
    }

    public static TradeDiary of(LocalDate time, KiwoomTradeDiaryResponse response){
        TradeDiary tradeDiary = TradeDiary.builder()
                .tradeDay(time)
                .totSellAmt(Double.valueOf(response.totalSellAmount()))
                .totBuyAmt(Double.valueOf(response.totalBuyAmount()))
                .totCmsnTax(Double.valueOf(response.totalCmsnTax()))
                .totExctAmt(Double.valueOf(response.totalExctAmount()))
                .totPlAmt(Double.valueOf(response.totalPlAmount()))
                .totPrftRt(Double.valueOf(response.totalProfitRate()))
                .build();

        List<TodayTradeItem> details = response.tradeDiaryList().stream()
                .map(item -> {
                    TodayTradeItem detail = TodayTradeItem.from(item);
                    tradeDiary.addTodayTradeDiary(detail);
                    return detail;
                })
                .toList();
        return tradeDiary;
    }
}
