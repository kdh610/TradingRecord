package com.tradingRecord.tradingRecord.application.dto.login;

public record TokenResponse(
        String token,
        String expires_dt,
        String token_type
) {
}
