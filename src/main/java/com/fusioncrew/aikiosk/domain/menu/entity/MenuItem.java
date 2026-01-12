package com.fusioncrew.aikiosk.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "menu_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_item_id", nullable = false, unique = true)
    private String menuItemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean hidden;

    // --- [추가된 필드] ---
    @Column(name = "category_id", nullable = false)
    private String categoryId; // 예: cat_burger

    @Column(name = "image_url")
    private String imageUrl; // 예: https://...
    // --------------------

    // [New] 메뉴 설명 (상세 조회용)
    @Column(length = 500) // 넉넉하게 잡음
    private String description;

    @Builder
    public MenuItem(String menuItemId, String name, int price, boolean hidden, String categoryId, String imageUrl,
            String description) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.hidden = hidden;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // [New] 다대다 매핑 (중간 테이블 자동 생성: menu_ingredient_links)
    @ManyToMany
    @JoinTable(name = "menu_ingredient_links", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    // [핵심] A안: 덮어쓰기(Replace) 로직
    // 기존 목록을 비우고(clear), 새로운 리스트로 채웁니다.
    public void updateIngredients(List<Ingredient> newIngredients) {
        this.ingredients.clear();
        this.ingredients.addAll(newIngredients);
    }

    // 조회용 Getter (Lombok @Getter가 있다면 생략 가능)
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    // [New] 부분 업데이트 메서드 (PATCH용)
    // 들어온 값(null이 아닌 값)만 변경합니다.
    public void updateDetails(Integer price, String imageUrl, Boolean hidden, String description, String name,
            String categoryId) {
        if (price != null) {
            this.price = price;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
        if (hidden != null) {
            this.hidden = hidden;
        }
        if (description != null) {
            this.description = description;
        }
        if (name != null) {
            this.name = name;
        }
        if (categoryId != null) {
            this.categoryId = categoryId;
        }
    }

    // [New] 단일 재료 제거 메서드
    public boolean removeIngredient(Ingredient ingredient) {
        return this.ingredients.remove(ingredient);
    }

    // [New] 특정 ingredientId로 재료 제거
    public boolean removeIngredientById(String ingredientId) {
        return this.ingredients.removeIf(ing -> ing.getIngredientId().equals(ingredientId));
    }
}
