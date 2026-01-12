package com.fusioncrew.aikiosk.domain.order.service;

import com.fusioncrew.aikiosk.domain.order.dto.OrderDetailResponseDto;
import com.fusioncrew.aikiosk.domain.order.dto.OrderResponseDto;
import com.fusioncrew.aikiosk.domain.order.dto.OrderStatusUpdateRequestDto;
import com.fusioncrew.aikiosk.domain.order.dto.OrderStatusUpdateResponseDto;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.global.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrderList() {
        return orderRepository.findAll().stream()
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderStatusUpdateResponseDto updateOrderStatus(String orderId, OrderStatusUpdateRequestDto requestDto) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        OrderStatus previousStatus = order.updateStatusWithValidation(requestDto.getStatus(), requestDto.getNote());

        return OrderStatusUpdateResponseDto.builder()
                .orderId(orderId)
                .previousStatus(previousStatus.name())
                .currentStatus(order.getStatus().name())
                .note(requestDto.getNote())
                .updatedBy(OrderStatusUpdateResponseDto.UpdatedByDto.builder()
                        .adminUserId("adm_0001") // TODO: 실제 인증된 관리자 정보로 교체
                        .username("admin01")
                        .build())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}