package com.tradingRecord.tradingRecord.application.dto.kiwoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tradingRecord.tradingRecord.domain.entity.TodayTradeItem;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public record KiwoomDailyStockProfitResponse(
        @JsonProperty("dt_stk_div_rlzt_pl") List<KiwoomDailyStockProfitItem> dtStkDivRlztPl // 일자별 종목별 매매 리스트
) {

    public Optional<TodayTradeItem> calculateProfit(LocalDate date){
        double totBuyAmount = 0;
        double totSellAmount = 0;
        double totStockCount = 0;
        double totRlzProfit = 0;

        for(KiwoomDailyStockProfitItem item: this.dtStkDivRlztPl){
            totBuyAmount += Double.parseDouble(item.buyUnitPrice()) * Double.parseDouble(item.quantity());
            totSellAmount +=  Double.parseDouble(item.sellPrice()) * Double.parseDouble(item.quantity());
            totStockCount+= Double.parseDouble(item.quantity()); //총 주식수
            totRlzProfit+=Double.parseDouble(item.netProfit()); //총 실현 손익
        }
        if(totSellAmount==0 || totBuyAmount==0 || totSellAmount==0){
            return Optional.empty();
        }

        double avgBuyPrice = Math.round(totBuyAmount / totStockCount);
        double avgSellPrice = Math.round(totSellAmount / totStockCount);
        double profitRate = (totRlzProfit/totBuyAmount) *100;
        profitRate = Math.round(profitRate*100)/100.0;


        log.info("총 매수 수량 {}", totStockCount);
        log.info("평균 매수가 {}", avgBuyPrice);
        log.info("평균 매도가 {}", avgSellPrice);
        log.info("총 실현선익 {}", totRlzProfit);
        log.info("총 수익률 {}", profitRate);

        return Optional.of(TodayTradeItem.builder()
                .tradeDay(date)
                .stkNm(dtStkDivRlztPl.getFirst().stockName())
                .buyAvgPric(avgBuyPrice)
                .selAvgPric(avgSellPrice)
                .plAmt(totRlzProfit)
                .prftRt(profitRate)
                .stkCd(dtStkDivRlztPl.getFirst().stockCode())
                .build()
        );


    }


}
