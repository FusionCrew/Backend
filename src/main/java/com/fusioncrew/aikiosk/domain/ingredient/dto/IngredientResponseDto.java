package com.fusioncrew.aikiosk.domain.ingredient.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.AllergyTag;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import lombok.Getter;

@Getter
public class IngredientResponseDto {
    private final String ingredientId;
    private final String name;
    private final AllergyTag allergyTag; // JSON으로 나갈 땐 "MILK" 문자열로 자동 변환됨

    private final int calories;
    private final int extraPrice;

    public IngredientResponseDto(Ingredient ingredient) {
        this.ingredientId = ingredient.getIngredientId();
        this.name = ingredient.getName();
        this.allergyTag = ingredient.getAllergyTag();
        this.calories = ingredient.getCalories();
        this.extraPrice = ingredient.getExtraPrice();
    }
}