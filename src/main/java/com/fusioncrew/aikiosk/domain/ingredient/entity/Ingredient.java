package com.fusioncrew.aikiosk.domain.ingredient.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ingredients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ingredientId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "allergy_tag")
    private AllergyTag allergyTag;

    // [추가 1] 영양 정보 (API 명세 반영)
    @Column(nullable = false)
    private int calories;

    // [추가 2] 추가 비용 (클래스 다이어그램 반영, 옵션 가격용)
    @Column(nullable = false)
    private int extraPrice;
    // [New] 상세 영양 정보 (명세서의 nutrition 객체 대응)
    @Column(nullable = false, columnDefinition = "int default 0")
    private int protein; // 단백질(g)

    @Column(nullable = false, columnDefinition = "int default 0")
    private int sodium; // 나트륨(mg)

    @Builder
    public Ingredient(String ingredientId, String name, AllergyTag allergyTag, int calories, int extraPrice,
            int protein, int sodium) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.allergyTag = allergyTag;
        this.calories = calories;
        this.extraPrice = extraPrice;
        this.protein = protein;
        this.sodium = sodium;
    }

    // [New] 부분 업데이트 메서드 (PATCH용)
    public void updateDetails(String name, AllergyTag allergyTag, Integer calories, Integer extraPrice) {
        if (name != null) {
            this.name = name;
        }
        if (allergyTag != null) {
            this.allergyTag = allergyTag;
        }
        if (calories != null) {
            this.calories = calories;
        }
        if (extraPrice != null) {
            this.extraPrice = extraPrice;
        }
    }
}
