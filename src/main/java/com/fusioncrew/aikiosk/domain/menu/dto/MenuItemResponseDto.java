package com.fusioncrew.aikiosk.domain.menu.dto;

import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import lombok.Getter;

@Getter
public class MenuItemResponseDto {
    private final String menuItemId;
    private final String name;
    private final String nameEn;
    private final int price;
    private final boolean hidden;

    // Entity -> DTO 변환
    public MenuItemResponseDto(MenuItem menuItem) {
        this.menuItemId = menuItem.getMenuItemId();
        this.name = menuItem.getName();
        this.nameEn = menuItem.getNameEn();
        this.price = menuItem.getPrice();
        this.hidden = menuItem.isHidden();
    }
}