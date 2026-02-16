package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderDtos {

        public record CreateOrderRequest(
                        String cartId,
                        String sessionId,
                        String orderType,
                        String memo) {
        }

        public record Amount(int totalPrice, String currency) {
        }

        public record OrderCreateResponse(
                        String orderId,
                        Integer orderNumber,
                        String status,
                        Amount amount) {
        }

        public record OrderItemResponse(Long itemId, String menuItemId, int quantity, String optionsJson) {
        }

        public record OrderResponse(
                        Long orderId,
                        String sessionId,
                        OrderStatus status,
                        List<OrderItemResponse> items,
                        OffsetDateTime createdAt,
                        OffsetDateTime updatedAt) {
        }

        // 주문 상세 조회 응답 (GET /orders/{orderId}) - 명세 기준
        public record OrderItemDetailResponse(
                        String orderItemId,
                        String menuItemId,
                        String name,
                        int quantity,
                        int unitPrice,
                        int lineTotal) {
        }

        public record OrderGetResponse(
                        String orderId,
                        String status,
                        List<OrderItemDetailResponse> items,
                        Amount amount) {
        }

        public record OrderCancelRequest(String reason) {
        }

        public record OrderCancelResponse(String orderId, String status) {
        }

        public record OrderConfirmRequest(boolean agreeToPolicy) {
        }

        public record OrderConfirmResponse(String orderId, String status) {
        }
}
