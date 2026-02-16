package com.fusioncrew.aikiosk.domain.menu.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.ingredient.entity.AllergyTag;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
public class MenuDetailKioskResponseDto {
    private final String menuItemId;
    private final String name;
    private final String description;
    private final int price;
    private final Nutrition nutrition;
    private final List<String> ingredients;
    private final List<String> allergies;
    private final List<OptionGroup> options;

    @Builder
    public MenuDetailKioskResponseDto(String menuItemId, String name, String description, int price,
            Nutrition nutrition, List<String> ingredients, List<String> allergies, List<OptionGroup> options) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.nutrition = nutrition;
        this.ingredients = ingredients;
        this.allergies = allergies;
        this.options = options;
    }

    public static MenuDetailKioskResponseDto fromEntity(MenuItem menuItem, List<OptionGroup> options) {
        final String ALLERGEN_PREFIX = "__ALLERGEN__";

        // User-facing ingredients should not include system markers like "__ALLERGEN__" / "__NUTRITION__".
        List<String> ingredients = menuItem.getIngredients().stream()
                .map(Ingredient::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(n -> !n.isEmpty())
                .filter(n -> !n.startsWith("__"))
                .distinct()
                .collect(Collectors.toList());

        // Prefer explicit allergen markers so we can return Korean strings without enum expansion.
        // Fallback to allergyTag enum name() for legacy data.
        List<String> allergies = menuItem.getIngredients().stream()
                .map(Ingredient::getName)
                .filter(n -> n != null && n.trim().startsWith(ALLERGEN_PREFIX))
                .map(n -> n.trim().substring(ALLERGEN_PREFIX.length()).trim())
                .filter(n -> !n.isEmpty())
                .collect(Collectors.toList());

        if (allergies.isEmpty()) {
            allergies = menuItem.getIngredients().stream()
                    .filter(i -> i.getAllergyTag() != null && i.getAllergyTag() != AllergyTag.NONE)
                    .map(i -> i.getAllergyTag().name())
                    .collect(Collectors.toList());
        }

        // stable de-dupe
        allergies = new ArrayList<>(new TreeSet<>(allergies));

        return MenuDetailKioskResponseDto.builder()
                .menuItemId(menuItem.getMenuItemId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .nutrition(calculateTotalNutrition(menuItem.getIngredients()))
                .ingredients(ingredients)
                .allergies(allergies)
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
