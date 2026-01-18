package com.fusioncrew.aikiosk.domain.kiosk.entity;

public enum EndReason {
    USER_FINISHED, // 사용자가 주문/이용을 정상적으로 완료
    TIMEOUT, // 일정 시간 입력이 없어 자동 종료
    ADMIN_TERMINATED, // 관리자에 의해 강제 종료
    SYSTEM_ERROR // 시스템 오류로 인한 종료
}
