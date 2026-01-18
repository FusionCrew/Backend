package com.fusioncrew.aikiosk.domain.kiosk.entity;

public enum EventType {
    HESITATION, // 사용자 주저함 감지
    STT_FAILED, // 음성 인식 실패
    TTS_PLAYED, // 음성 안내 출력
    HAND_SIGN_DETECTED, // 수화 입력 감지
    STAFF_CALLED, // 직원 호출
    SYSTEM_NOTICE // 시스템 안내 이벤트
}
