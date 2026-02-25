package com.tradingRecord.tradingRecord.infrastructure.DB;

import java.time.LocalDate;
import java.util.UUID;

public interface TradeSummary {
    UUID getId();
    LocalDate getTradeDay();
    String getStkNm();
    String getTradingType();
    boolean isWinLose();
    Double getPlAmt();
    Double getPrftRt();
    boolean isStupid();
}
