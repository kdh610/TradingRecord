package com.tradingRecord.tradingRecord.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "time")
    private LocalDateTime time;

    private String stkBondTp;
    private String ordNo;
    private String stkCd;
    private String trdeTp;
    private String ioTpNm;
    private Double ordQty;
    private Double ordUv;
    private String cnfmQty;
    private String rsrvOppo;
    private String cntrNo;
    private String acptTp;
    private String origOrdNo;
    private String stkNm;
    private String setlTp;
    private String crdDealTp;
    private Double cntrQty;
    private Double cntrUv;
    private String commOrdTp;
    private String mdfyCnclTp;
    private String cntrTm;
    private String dmstStexTp;
    private String condUv;
}
