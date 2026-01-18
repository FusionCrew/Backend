package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderDetailResponseDto {
    private String orderId;
    private OrderStatus status;
    private List<OrderItemDto> items;
    private int totalPrice;
    private LocalDateTime createdAt;
    private PaymentDto payment;
    private String sessionId;

    @Getter
    @Builder
    public static class OrderItemDto {
        private String menuItemId;
        private String name;
        private int price;
        private int quantity;

        public static OrderItemDto from(OrderItem item) {
            return OrderItemDto.builder()
                    .menuItemId(item.getMenuItemId())
                    .name(item.getName())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PaymentDto {
        private String method;
        private String status;
    }

    public static OrderDetailResponseDto from(Order order) {
        return OrderDetailResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(OrderItemDto::from)
                        .collect(Collectors.toList()))
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .sessionId(order.getSessionId())
                // 현재는 Mock 결제 정보 제공
                .payment(PaymentDto.builder()
                        .method("MOCK")
                        .status("PAID")
                        .build())
                .build();
    }
}
