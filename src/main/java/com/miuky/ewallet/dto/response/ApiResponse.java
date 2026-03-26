package com.miuky.ewallet.dto.response;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {
    public static <T> ApiResponse<T> fail(T errors) {
        return new ApiResponse<>(false, null, errors, Instant.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "", data, Instant.now());
    }

}
