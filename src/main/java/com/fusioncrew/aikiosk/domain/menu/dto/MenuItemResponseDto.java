package com.fusioncrew.aikiosk.domain.menu.dto;

import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup;
import com.fusioncrew.aikiosk.domain.menu.entity.OptionItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MenuItemResponseDto {
    private final String menuItemId;
    private final String name;
    private final String nameEn;
    private final int price;
    private final boolean hidden;
    private final String categoryId;
    private final String imageUrl;
    private final String description;
    private final List<IngredientInfo> ingredients;
    private final List<OptionGroupInfo> optionGroups;

    @Getter
    public static class IngredientInfo {
        private final String ingredientId;
        private final String name;
        private final String allergyTag;
        private final int calories;
        private final int extraPrice;

        public IngredientInfo(Ingredient ingredient) {
            this.ingredientId = ingredient.getIngredientId();
            this.name = ingredient.getName();
            this.allergyTag = ingredient.getAllergyTag() != null ? ingredient.getAllergyTag().name() : null;
            this.calories = ingredient.getCalories();
            this.extraPrice = ingredient.getExtraPrice();
        }
    }

    @Getter
    public static class OptionGroupInfo {
        private final String name;
        @JsonProperty("isRequired")
        private final boolean isRequired;
        @JsonProperty("isMultipleSelectionAllowed")
        private final boolean isMultipleSelectionAllowed;
        private final List<OptionItemInfo> items;

        public OptionGroupInfo(OptionGroup group) {
            this.name = group.getName();
            this.isRequired = group.isRequired();
            this.isMultipleSelectionAllowed = group.isMultipleSelectionAllowed();
            this.items = group.getOptionItems().stream().map(OptionItemInfo::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class OptionItemInfo {
        private final String name;
        private final int extraPrice;

        public OptionItemInfo(OptionItem item) {
            this.name = item.getName();
            this.extraPrice = item.getExtraPrice();
        }
    }

    // Entity -> DTO 변환
    public MenuItemResponseDto(MenuItem menuItem) {
        this.menuItemId = menuItem.getMenuItemId();
        this.name = menuItem.getName();
        this.nameEn = menuItem.getNameEn();
        this.price = menuItem.getPrice();
        this.hidden = menuItem.isHidden();
        this.categoryId = menuItem.getCategoryId();
        this.imageUrl = menuItem.getImageUrl();
        this.description = menuItem.getDescription();
        this.ingredients = menuItem.getIngredients() == null ? new ArrayList<>()
                : menuItem.getIngredients().stream()
                        .map(IngredientInfo::new)
                        .collect(Collectors.toList());
        this.optionGroups = menuItem.getOptionGroups() == null ? new ArrayList<>()
                : menuItem.getOptionGroups().stream()
                        .map(OptionGroupInfo::new)
                        .collect(Collectors.toList());
    }
}