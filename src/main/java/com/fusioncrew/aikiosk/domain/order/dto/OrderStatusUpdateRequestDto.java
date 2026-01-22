package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

public record OrderStatusUpdateRequestDto(OrderStatus status, String note) {
}