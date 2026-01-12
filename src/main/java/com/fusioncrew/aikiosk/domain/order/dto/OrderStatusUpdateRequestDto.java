package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateRequestDto {
    private OrderStatus status;
    private String note;
}