package com.fusioncrew.aikiosk.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderStatusUpdateResponseDto {
    private String orderId;
    private String previousStatus;
    private String currentStatus;
    private String note;
    private UpdatedByDto updatedBy;
    private LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class UpdatedByDto {
        private String adminUserId;
        private String username;
    }
}
