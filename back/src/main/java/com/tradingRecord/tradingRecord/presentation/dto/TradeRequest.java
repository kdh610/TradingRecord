package com.tradingRecord.tradingRecord.presentation.dto;

import java.util.List;
import java.util.UUID;

public record TradeRequest(
        String stkNm,
        String tradeType,
        Boolean isStupid,
        List<UUID> orderLogIds
) {
}
