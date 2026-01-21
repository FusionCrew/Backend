package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminTicketListResponse {
    private List<TicketItem> items;
    private TicketSummary summary;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class TicketItem {
        private String ticketId;
        private int number;
        private String status;
        private String priority;
        private String orderId;
        private String orderStatus;
        private String kioskId;
        private LocalDateTime issuedAt;
        private LocalDateTime calledAt;
        private LocalDateTime completedAt;
        private long waitingSeconds;
    }

    @Getter
    @Builder
    public static class TicketSummary {
        private long total;
        private long waiting;
        private long called;
        private long completed;
    }
}
