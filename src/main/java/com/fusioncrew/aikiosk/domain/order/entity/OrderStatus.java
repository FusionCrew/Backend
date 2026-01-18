package com.fusioncrew.aikiosk.domain.order.entity;

public enum OrderStatus {
    PENDING,    // 주문 생성
    CONFIRMED,  // 확정
    MAKING,     // 조리 중
    READY,      // 준비 완료
    CANCELLED   // 취소
}