package com.fusioncrew.aikiosk.global.api;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        boolean success,
        T data,
        String timestamp,
        String requestId
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, OffsetDateTime.now().toString(), RequestId.next());
    }
}