package com.fusioncrew.aikiosk.domain.ingredient.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.AllergyTag;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class IngredientCreateRequestDto {

    private String name; // 필수: 계란
    private AllergyTag allergyTag; // 선택: EGG (없으면 null)
    private Nutrition nutrition; // 선택: 영양 정보 객체 (기존 호환용)

    // [New] 프론트엔드에서 직접 받을 수 있도록 추가
    private Integer calories; // 선택: 칼로리 (nutrition.kcal 대신)
    private Integer extraPrice; // 선택: 추가가격

    @Getter
    @NoArgsConstructor
    public static class Nutrition {
        private Integer kcal; // 선택: 155
    }

    // Entity로 변환하는 메서드
    // ID는 서비스 계층에서 생성해서 주입받음
    public Ingredient toEntity(String generatedId) {
        // calories 우선순위: 직접 입력 > nutrition.kcal > 기본값 0
        int caloriesValue = 0;
        if (this.calories != null) {
            caloriesValue = this.calories;
        } else if (nutrition != null && nutrition.getKcal() != null) {
            caloriesValue = nutrition.getKcal();
        }

        // extraPrice: 직접 입력 > 기본값 0
        int extraPriceValue = (this.extraPrice != null) ? this.extraPrice : 0;

        return Ingredient.builder()
                .ingredientId(generatedId)
                .name(this.name)
                .allergyTag(this.allergyTag)
                .calories(caloriesValue)
                .extraPrice(extraPriceValue)
                .protein(0)
                .sodium(0)
                .build();
    }
}
