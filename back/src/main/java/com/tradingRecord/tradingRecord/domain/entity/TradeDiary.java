package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomDailyRealProfitResponse;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomTradeDiaryResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="trade_diary")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TradeDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(unique = true)
    private LocalDate tradeDay;

    private Double totSellAmt;//총매도금액
    private Double totBuyAmt; //총매수금액
    private Double rlztPl; //실현손익
    private Double trdeCmsn; //매매수수료
    private Double trdeTax; //매매세금
    private Double totPrftRt; //수익률

    @Builder.Default
    @OneToMany(mappedBy = "tradeDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodayTradeItem> todayTradeItemList = new ArrayList<>();

    public void addTodayTradeDiary(TodayTradeItem todayTradeItem){
        this.todayTradeItemList.add(todayTradeItem);
        if(todayTradeItem.getTradeDiary() != this){
            todayTradeItem.setTradeDiary(this);
        }
    }

    public static TradeDiary of(LocalDate time, KiwoomDailyRealProfitResponse dailyRealProfit){
        TradeDiary newTradeDiary = TradeDiary.builder()
                .tradeDay(time)
                .totSellAmt(Double.valueOf(dailyRealProfit.totalSellAmount()))
                .totBuyAmt(Double.valueOf(dailyRealProfit.totalBuyAmount()))
                .rlztPl(Double.valueOf(dailyRealProfit.rlztPl()))
                .trdeCmsn(Double.valueOf(dailyRealProfit.trdeCmsn()))
                .trdeTax(Double.valueOf(dailyRealProfit.trdeTax()))
                .build();

        return newTradeDiary;
    }



}
