package com.tradingRecord.tradingRecord.domain.entity;

import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomOrderLogItem;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.KiwoomOrderLogResponse;
import com.tradingRecord.tradingRecord.application.dto.kiwoom.OrderLogRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Column(name = "trade_day")
    private LocalDate tradeDay;

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
