package com.fusioncrew.aikiosk.domain.menu.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
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
    public MenuDetailKioskResponseDto(String menuItemId, String name, String description, int price,
            Nutrition nutrition, List<String> allergies, List<OptionGroup> options) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.nutrition = nutrition;
        this.allergies = allergies;
        this.options = options;
    }

    public static MenuDetailKioskResponseDto fromEntity(MenuItem menuItem, List<OptionGroup> options) {
        return MenuDetailKioskResponseDto.builder()
                .menuItemId(menuItem.getMenuItemId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .nutrition(calculateTotalNutrition(menuItem.getIngredients()))
                .allergies(menuItem.getIngredients().stream()
                        .filter(i -> i.getAllergyTag() != null)
                        .map(i -> i.getAllergyTag().name())
                        .distinct()
                        .collect(Collectors.toList()))
                .options(options)
                .build();
    }

    private static Nutrition calculateTotalNutrition(List<Ingredient> ingredients) {
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

    @Getter
    public static class Nutrition {
        private final int kcal;
        private final int proteinG;
        private final int sodiumMg;

        public Nutrition(int kcal, int proteinG, int sodiumMg) {
            this.kcal = kcal;
            this.proteinG = proteinG;
            this.sodiumMg = sodiumMg;
        }
    }

    @Getter
    @Builder
    public static class OptionGroup {
        @JsonProperty("optionGroup")
        private final String optionGroup; // "SIZE", "SET", "EXTRA"
        @JsonProperty("isRequired")
        private final boolean isRequired;
        @JsonProperty("isMultipleSelectionAllowed")
        private final boolean isMultipleSelectionAllowed;
        private final List<OptionChoice> choices;
    }

    @Getter
    @Builder
    public static class OptionChoice {
        private final String name;
        private final int extraPrice;
    }
}