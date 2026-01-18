package com.fusioncrew.aikiosk.domain.ingredient.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.AllergyTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IngredientUpdateRequestDto {
    // 모든 필드 선택적 (부분 수정 지원)
    private String name;
    private AllergyTag allergyTag;
    private Integer calories;
    private Integer extraPrice;
}
