package com.tradingRecord.tradingRecord.infrastructure.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    SUCCESS(HttpStatus.OK,  "성공적으로 처리되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에러"),

    KIWOOM_REST_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "키움 API 에러"),
    KIWOOM_TRADE_DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜의 매매일지가 없습니다 : "),
    KIWOOM_REALIZED_PROFIT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜의 매매일지가 없습니다 : "),
    KIWOOM_ORDER_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜 해당 종목의 주문체결로그가 없습니다 : "),
    KIWOOM_TRADE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜 해당 종목의 매매가 없습니다 : "),
    KIWOOM_CANDLE_NOT_FOUND(HttpStatus.NOT_FOUND, "1분봉 캔들을 가져오는 중 에러 발생 : "),

    TRADE_DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜의 매매일지가 없습니다."),
    REALIZED_PROFIT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜의 매매일지가 없습니다."),
    ORDER_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜 해당 종목의 주문체결로그가 없습니다."),
    TRADE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 날짜 해당 종목의 매매가 없습니다."),



    ;



    private final HttpStatus status;
    private final String message;

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}
