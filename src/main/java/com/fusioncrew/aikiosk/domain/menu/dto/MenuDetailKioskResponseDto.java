package com.fusioncrew.aikiosk.domain.menu.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MenuDetailKioskResponseDto {
    private final String menuItemId;
    private final String name;
    private final String description;
    private final int price;
    private final Nutrition nutrition;
    private final List<String> allergies;
    private final List<OptionGroup> options;

    @Builder
    public MenuDetailKioskResponseDto(MenuItem menuItem, List<OptionGroup> options) {
        this.menuItemId = menuItem.getMenuItemId();
        this.name = menuItem.getName();
        this.description = menuItem.getDescription();
        this.price = menuItem.getPrice();

        // 영양 정보 계산 (재료들의 합)
        this.nutrition = calculateTotalNutrition(menuItem.getIngredients());

        // 알레르기 정보 추출 (중복 제거)
        this.allergies = menuItem.getIngredients().stream()
                .filter(i -> i.getAllergyTag() != null) // NONE 제외 로직이 필요하다면 추가
                .map(i -> i.getAllergyTag().name()) // Enum -> String ("MILK")
                .distinct()
                .collect(Collectors.toList());

        this.options = options;
    }

    // 재료 리스트를 순회하며 영양소 합산
    private Nutrition calculateTotalNutrition(List<Ingredient> ingredients) {
        int totalKcal = 0;
        int totalProtein = 0;
        int totalSodium = 0;

        for (Ingredient i : ingredients) {
            totalKcal += i.getCalories();
            totalProtein += i.getProtein();
            totalSodium += i.getSodium();
        }
        return new Nutrition(totalKcal, totalProtein, totalSodium);
    }

    // --- 내부 클래스 (JSON 중첩 구조용) ---
    @Getter
    public static class Nutrition {
        private final int kcal;
        private final int proteinG; // JSON 필드명: proteinG
        private final int sodiumMg; // JSON 필드명: sodiumMg

        public Nutrition(int kcal, int proteinG, int sodiumMg) {
            this.kcal = kcal;
            this.proteinG = proteinG;
            this.sodiumMg = sodiumMg;
        }
    }

    @Getter
    @Builder
    public static class OptionGroup {
        private final String optionGroup; // "SIZE", "SET", "EXTRA"
        private final List<String> choices; // ["SINGLE", "DOUBLE"]
    }
}