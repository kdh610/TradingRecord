package com.tradingRecord.tradingRecord.infrastructure.exception;

import com.tradingRecord.tradingRecord.infrastructure.common.ApiResponse;
import com.tradingRecord.tradingRecord.infrastructure.common.Code;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<String>> handlerBaseExceptionException(BaseException e) {
        Code code = e.getErrorCode();
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.of(code.getMessage(),null));
    }

}

