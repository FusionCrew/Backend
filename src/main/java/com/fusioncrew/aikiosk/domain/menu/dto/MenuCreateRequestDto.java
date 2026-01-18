package com.fusioncrew.aikiosk.domain.menu.dto;

import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuCreateRequestDto {

    private String name; // 필수: 불고기버거_세트
    private int price; // 필수: 8500
    private String categoryId; // 필수: cat_set
    private boolean hidden; // 필수: false
    private String imageUrl; // 선택
    private String description; // 선택

    // DTO -> Entity 변환
    public MenuItem toEntity(String generatedId) {
        return MenuItem.builder()
                .menuItemId(generatedId)
                .name(this.name)
                .price(this.price)
                .categoryId(this.categoryId)
                .hidden(this.hidden)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .build();
    }
}