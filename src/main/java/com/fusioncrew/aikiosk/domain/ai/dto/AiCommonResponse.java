package com.fusioncrew.aikiosk.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiCommonResponse<T> {
    private boolean success;
    private T data;
    private String timestamp;
    private String requestId;
}
