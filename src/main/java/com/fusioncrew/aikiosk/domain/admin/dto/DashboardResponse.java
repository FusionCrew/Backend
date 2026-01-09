package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private boolean success;
    private DashboardSummaryDto data;
    private String timestamp;
    private String requestId;

    public static DashboardResponse success(DashboardSummaryDto data, String requestId) {
        return DashboardResponse.builder()
                .success(true)
                .data(data)
                .timestamp(ZonedDateTime.now().toString())
                .requestId(requestId)
                .build();
    }
}
