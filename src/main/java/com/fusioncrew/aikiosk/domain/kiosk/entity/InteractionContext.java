package com.fusioncrew.aikiosk.domain.kiosk.entity;

public enum InteractionContext {
    MENU_BROWSING, // 메뉴 탐색 중
    ORDER_CONFIRMATION, // 주문 확인 단계
    PAYMENT, // 결제 단계
    IDLE // 대기 상태
}
