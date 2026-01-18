package com.fusioncrew.aikiosk.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class MenuIngredientLinkRequestDto {
    private List<String> ingredientIds; // 예: ["ing_01", "ing_02"]
}