package com.tradingRecord.tradingRecord.presentation.dto;

import java.util.UUID;

public record MarketTrendRequest(
        String trend,
        UUID id
) {
}
