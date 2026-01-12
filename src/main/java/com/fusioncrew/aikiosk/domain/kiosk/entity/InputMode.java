package com.fusioncrew.aikiosk.domain.kiosk.entity;

public enum InputMode {
    TOUCH, // 터치 입력
    VOICE, // 음성 입력 (STT 연동)
    SIGN_LANGUAGE // 수어 입력 (Vision 연동)
}
