package com.fusioncrew.aikiosk.domain.ingredient.controller;

import com.fusioncrew.aikiosk.domain.ingredient.dto.IngredientCreateRequestDto;
import com.fusioncrew.aikiosk.domain.ingredient.dto.IngredientResponseDto;
import com.fusioncrew.aikiosk.domain.ingredient.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/ingredients")
@RequiredArgsConstructor
public class IngredientAdminController {

    private final IngredientService ingredientService;

    // 조회 (GET)
    @GetMapping
    public Map<String, Object> getIngredients() {
        List<IngredientResponseDto> items = ingredientService.getIngredientList();

        Map<String, Object> data = new HashMap<>();
        data.put("items", items);

        return createSuccessResponse(data);
    }

    // [New] 등록 (POST)
    @PostMapping
    public org.springframework.http.ResponseEntity<Map<String, Object>> createIngredient(
            @RequestBody IngredientCreateRequestDto request) {
        // 1. 서비스 호출
        String newIngredientId = ingredientService.createIngredient(request);

        // 2. 응답 데이터 구성 ({"ingredientId": "ing_xx"})
        Map<String, Object> data = new HashMap<>();
        data.put("ingredientId", newIngredientId);

        // 3. 201 Created 응답
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(createSuccessResponse(data));
    }

    // 공통 응답 생성 헬퍼 메서드
    private Map<String, Object> createSuccessResponse(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return response;
    }

    // [New] 단일 재료 조회 (GET)
    @GetMapping("/{ingredientId}")
    public Map<String, Object> getIngredientDetail(@PathVariable String ingredientId) {
        var ingredient = ingredientService.getIngredientDetail(ingredientId);

        Map<String, Object> data = new HashMap<>();
        data.put("ingredientId", ingredient.getIngredientId());
        data.put("name", ingredient.getName());
        data.put("allergyTag", ingredient.getAllergyTag());
        data.put("calories", ingredient.getCalories());
        data.put("extraPrice", ingredient.getExtraPrice());

        return createSuccessResponse(data);
    }

    // [New] 재료 수정 (PATCH)
    @PatchMapping("/{ingredientId}")
    public Map<String, Object> updateIngredient(
            @PathVariable String ingredientId,
            @RequestBody com.fusioncrew.aikiosk.domain.ingredient.dto.IngredientUpdateRequestDto request) {

        var updatedIngredient = ingredientService.updateIngredient(ingredientId, request);

        Map<String, Object> data = new HashMap<>();
        data.put("ingredientId", updatedIngredient.getIngredientId());
        data.put("name", updatedIngredient.getName());
        data.put("allergyTag", updatedIngredient.getAllergyTag());
        data.put("calories", updatedIngredient.getCalories());
        data.put("extraPrice", updatedIngredient.getExtraPrice());

        return createSuccessResponse(data);
    }

    // [New] 재료 삭제 (DELETE)
    @DeleteMapping("/{ingredientId}")
    public Map<String, Object> deleteIngredient(@PathVariable String ingredientId) {
        // 삭제 수행
        ingredientService.deleteIngredient(ingredientId);

        // 응답 데이터 구성 (명세서 준수)
        Map<String, Object> data = new HashMap<>();
        data.put("deleted", true);
        data.put("ingredientId", ingredientId);

        return createSuccessResponse(data);
    }

    // [New] 특정 재료를 사용하는 메뉴 목록 조회 API (역방향 조회)
    @GetMapping("/{ingredientId}/menu-items")
    public Map<String, Object> getMenusByIngredient(@PathVariable String ingredientId) {
        var items = ingredientService.getMenusByIngredient(ingredientId);

        Map<String, Object> data = new HashMap<>();
        data.put("ingredientId", ingredientId);
        data.put("items", items);

        return createSuccessResponse(data);
    }
}
