package com.tradingRecord.tradingRecord.infrastructure.common;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data){
        return ApiResponse.<T>builder()
                .message(Code.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> of(String message, T data) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }


}
