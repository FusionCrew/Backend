package com.fusioncrew.aikiosk.domain.kiosk.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecommendationBasis {
    STORE_MANAGER_RECOMMENDATION("매장 운영자 추천"),
    AI_RECOMMENDATION("AI 맞춤 추천"),
    TRENDING("현재 인기 메뉴"),
    SEASONAL_PICK("시즌 한정 추천");

    private final String description;
}
