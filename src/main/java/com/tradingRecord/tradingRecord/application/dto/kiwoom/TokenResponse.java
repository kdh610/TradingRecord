package com.tradingRecord.tradingRecord.application.dto.kiwoom;

public record TokenResponse(
        String token,
        String expires_dt,
        String token_type
) {
}
