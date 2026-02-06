package com.fusioncrew.aikiosk.domain.order.entity;

public enum OrderCancelReason {
    USER_REQUEST, // 사용자 요청으로 취소 (변심 등)
    DUPLICATE_ORDER, // 중복 주문
    PAYMENT_FAILED, // 결제 실패로 인한 취소
    OUT_OF_STOCK, // 재고 부족으로 인한 취소
    SYSTEM_ERROR // 시스템 오류로 인한 취소
}
