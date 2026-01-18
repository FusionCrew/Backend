package com.fusioncrew.aikiosk.domain.ingredient.repository;

import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // ingredientId 순서대로 정렬해서 조회
    List<Ingredient> findAllByOrderByIngredientIdAsc();

    // [New] ID 중복 검사용
    boolean existsByIngredientId(String ingredientId);

    // [New] ID 목록에 포함된 모든 재료 조회 (WHERE ingredient_id IN (...))
    List<Ingredient> findAllByIngredientIdIn(List<String> ingredientIds);

    // [New] 단일 재료 조회용
    java.util.Optional<Ingredient> findByIngredientId(String ingredientId);
}
