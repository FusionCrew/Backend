package com.fusioncrew.aikiosk.global.api;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        boolean success,
        T data,
        String timestamp,
        String requestId) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
                true,
                data,
                java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        .format(OffsetDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS)),
                RequestId.next());
    }
}