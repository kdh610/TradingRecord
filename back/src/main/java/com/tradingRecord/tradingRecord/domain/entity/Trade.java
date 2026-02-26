package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.presentation.dto.TradeRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "trade")
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    private String stkNm;
    private String tradingType;
    private Double prftRt;
    private Double plAmt;
    private Boolean winLose;
    private Boolean stupid;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @Column(columnDefinition = "TEXT")
    private String review;
    private LocalDate tradeDay;

    @Builder.Default
    @OneToMany(mappedBy = "trade",cascade = CascadeType.ALL)
    private List<OrderLog> orderLogList = new ArrayList<>();

    /**
     * vector store에 매매체결 타점을 저장하기 위한 정보를 텍스트로 요약.
     */
    public String createOrderLogSummary(){
        StringBuffer stringBuffer = new StringBuffer();

        for(OrderLog orderLog: this.orderLogList){
            stringBuffer.append(orderLog.createOrderLogSummary());
        }

        return stringBuffer.toString();
    }

    /**
     * vector store에 매매정보를 저장하기 위한 텍스트 반환
     * @return
     */
    public String createTradeSummary(){
        return String.format(
                "id: %s, 날짜: %s, 종목: %s, 유형: %s, 수익률: %.2f%%, 뇌동여부: %s. 복기: %s.",
                this.getId(),
                this.getTradeDay(),
                this.getStkNm(),
                this.getTradingType(),
                this.getPrftRt(),
                this.getStupid() ? "Y" : "N",
                this.getReview()
        );
    }


    public static Trade processWinRate(List<OrderLog> orderLogs, TradeRequest requests){
        Trade newTrade = Trade.builder()
                .stkNm(requests.stkNm())
                .tradingType(requests.tradeType())
                .stupid(requests.isStupid())
                .comment(requests.comment())
                .review(requests.review())
                .tradeDay(requests.tradeDay())
                .build();
        for (OrderLog log : orderLogs) {
            newTrade.addOrderLog(log);
        }
        newTrade.calculateWinRate();
        return newTrade;
    }



    public void addOrderLog(OrderLog orderLog){
        this.orderLogList.add(orderLog);
        if(orderLog.getTrade() != this){
            orderLog.setTrade(this);
        }
    }

    public void calculateWinRate(){
        double curAvgPrice = 0;
        double curTotQty = 0;
        double curTotProfit = 0;
        double totalInvested = 0;

        for(OrderLog orderLog: orderLogList){
            log.info("orderlog {}",orderLog);
            log.info("isUsed {}",orderLog.getIsUsed());
            if(orderLog.getIsUsed()){
                throw new RuntimeException("이미 매매에 적용된 주문 존재");
            }

            if (orderLog.isBuyOrder()) {
                curAvgPrice =  orderLog.getNewAvgPrice(curAvgPrice, curTotQty);
                curTotQty = orderLog.plusTotQty(curTotQty);
                totalInvested += orderLog.getBuyAmount();
            }else if (orderLog.isSellOrder()) {
                double profitFromThisSale = orderLog.calProfitFromThisSale(curAvgPrice);
                curTotProfit += profitFromThisSale;
                curTotQty = orderLog.minusTotQty(curTotQty);
            }
            if (curTotQty <= 0) {
                curAvgPrice = 0;
                curTotQty = 0;
            }

            orderLog.setIsUsed(true);
        }

        this.plAmt =(double) Math.round(curTotProfit);
        this.prftRt = Math.round((curTotProfit / totalInvested) * 100.0 *100.0) /100.0;
        this.winLose = (curTotProfit > 0);

        log.info("수익금 {}",plAmt);
        log.info("수익룰 {}",prftRt);
        log.info("승패 {}",winLose);

    }

    public void setComment(String comment){
        this.comment = comment;
    }

}
