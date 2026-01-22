package com.fusioncrew.aikiosk.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {
    // ⚠️ 중요: int, boolean 대신 Integer, Boolean 사용!
    // 이유: 값이 안 들어왔을 때(null)와 0/false를 구분하기 위함
    private Integer price;
    private String imageUrl;
    private Boolean hidden;
    private String description;
    private String name;
    private String nameEn;
    private String categoryId;
}