package com.tradingRecord.tradingRecord.application.dto.kiwoom;

public record TokenRequest(
        String grant_type,
        String appkey,
        String secretkey
) {
}
