package com.tradingRecord.tradingRecord.application.dto.login;

public record TokenRequest(
        String grant_type,
        String appkey,
        String secretkey
) {
}
