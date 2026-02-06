package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminStockUpdateResponse {
    private String stockId;
    private Integer quantity;
}