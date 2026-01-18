package com.fusioncrew.aikiosk.domain.ingredient.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AllergyTag {
    MILK("우유"),
    EGG("계란"),
    WHEAT("밀"),
    SOY("대두"),
    PEANUT("땅콩"),
    NUT("견과류"),
    SESAME("참깨"),
    SHELLFISH("갑각류"),
    FISH("생선"),
    NONE("알레르기 없음");

    private final String description; // 한글 설명 (필요시 사용)
}
