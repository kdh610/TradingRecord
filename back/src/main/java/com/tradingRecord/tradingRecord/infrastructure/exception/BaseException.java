package com.tradingRecord.tradingRecord.infrastructure.exception;

import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import lombok.Getter;

import static com.tradingRecord.tradingRecord.infrastructure.common.Code.INTERNAL_SERVER_ERROR;

@Getter
public class BaseException extends RuntimeException{

    private final Code errorCode;

    public BaseException(String message) {
        super(message);
        this.errorCode = INTERNAL_SERVER_ERROR;
    }

    public BaseException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(Code errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
