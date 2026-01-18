package com.fusioncrew.aikiosk.domain.order.entity;

public enum OrderStatus {
    CREATED, // 주문 생성됨
    PENDING, // 결제 대기
    CONFIRMED, // 주문 확정 (접수)
    MAKING, // 조리 중
    READY, // 준비 완료 (픽업 대기)
    COMPLETED, // 수령 완료
    CANCELLED // 취소됨
}