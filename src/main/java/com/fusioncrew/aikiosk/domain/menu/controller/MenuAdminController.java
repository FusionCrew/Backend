package com.fusioncrew.aikiosk.domain.menu.controller;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuCreateRequestDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuItemResponseDto;
import com.fusioncrew.aikiosk.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuIngredientLinkRequestDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuUpdateRequestDto;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;

@RestController
@RequestMapping("/api/v1/admin/menu-items")
@RequiredArgsConstructor
public class MenuAdminController {

    private final MenuService menuService;

    @GetMapping
    public Map<String, Object> getMenuList() {
        // 1. 서비스 로직 수행
        List<MenuItemResponseDto> items = menuService.getAdminMenuList();

        // 2. 응답 데이터 구조 조립 
        Map<String, Object> data = new HashMap<>();
        data.put("items", items);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        // Request ID는 실제로는 Filter/Interceptor에서 생성하지만, 여기선 예시로 생성
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    // [New] 메뉴 등록 API
    @PostMapping
    public Map<String, Object> createMenu(@RequestBody MenuCreateRequestDto request) {
        // 1. 서비스 호출
        String createdMenuId = menuService.createMenu(request);

        // 2. data 구조 만들기
        Map<String, Object> data = new HashMap<>();
        data.put("menuItemId", createdMenuId);

        // 3. 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    // [New] 메뉴-재료 매핑 API
    @PostMapping("/{menuItemId}/ingredients")
    public Map<String, Object> linkIngredientsToMenu(
            @PathVariable String menuItemId,
            @RequestBody MenuIngredientLinkRequestDto request) {

        // 1. 서비스 호출 (업데이트 후 매핑된 ID 목록 받음)
        List<String> mappedIds = menuService.updateMenuIngredients(menuItemId, request);

        // 2. data 객체 조립
        Map<String, Object> data = new HashMap<>();
        data.put("menuItemId", menuItemId);
        data.put("mappedIngredientIds", mappedIds);

        // 3. 최종 응답 생성 (명세서 포맷 준수)
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    // [New] 메뉴 수정 API (PATCH)
    @PatchMapping("/{menuItemId}")
    public Map<String, Object> updateMenu(
            @PathVariable String menuItemId,
            @RequestBody MenuUpdateRequestDto request) {

        // 1. 서비스 호출 (수정된 Entity를 받음)
        MenuItem updatedMenu = menuService.updateMenu(menuItemId, request);

        // 2. data 객체 조립 (명세서 요구사항 반영)
        Map<String, Object> data = new HashMap<>();
        data.put("menuItemId", updatedMenu.getMenuItemId());
        data.put("name", updatedMenu.getName());
        data.put("price", updatedMenu.getPrice());
        data.put("categoryId", updatedMenu.getCategoryId());
        data.put("imageUrl", updatedMenu.getImageUrl());
        data.put("hidden", updatedMenu.isHidden());
        data.put("description", updatedMenu.getDescription());

        // 3. 최종 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    // [New] 단일 메뉴 조회 API (GET) - 재료 목록 포함
    @GetMapping("/{menuItemId}")
    public Map<String, Object> getMenuDetail(@PathVariable String menuItemId) {
        // 1. 서비스 호출
        MenuItemResponseDto menuItem = menuService.getMenuDetail(menuItemId);

        // 2. 재료 목록도 조회 (MenuItem Entity에서 직접)
        MenuItem menu = menuService.getMenuEntity(menuItemId);
        List<Map<String, Object>> ingredients = menu.getIngredients().stream()
                .map(ing -> {
                    Map<String, Object> ingMap = new HashMap<>();
                    ingMap.put("ingredientId", ing.getIngredientId());
                    ingMap.put("name", ing.getName());
                    ingMap.put("allergyTag", ing.getAllergyTag());
                    ingMap.put("calories", ing.getCalories());
                    ingMap.put("extraPrice", ing.getExtraPrice());
                    return ingMap;
                })
                .collect(java.util.stream.Collectors.toList());

        // 3. data 객체에 재료 목록 추가
        Map<String, Object> data = new HashMap<>();
        data.put("menuItemId", menuItem.getMenuItemId());
        data.put("name", menuItem.getName());
        data.put("price", menuItem.getPrice());
        data.put("hidden", menuItem.isHidden());
        data.put("imageUrl", menu.getImageUrl());
        data.put("categoryId", menu.getCategoryId());
        data.put("ingredients", ingredients);

        // 4. 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    // [New] 메뉴 삭제 API (DELETE)
    @DeleteMapping("/{menuItemId}")
    public Map<String, Object> deleteMenu(@PathVariable String menuItemId) {
        // 1. 서비스 호출 (삭제 수행)
        menuService.deleteMenu(menuItemId);

        // 2. 응답 데이터 구성 (명세서 준수)
        Map<String, Object> data = new HashMap<>();
        data.put("deleted", true);
        data.put("menuItemId", menuItemId);

        // 3. 최종 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    // [New] 메뉴-재료 매핑 삭제 API (DELETE)
    @DeleteMapping("/{menuItemId}/ingredients/{ingredientId}")
    public Map<String, Object> removeIngredientFromMenu(
            @PathVariable String menuItemId,
            @PathVariable String ingredientId) {

        // 1. 서비스 호출 (매핑 삭제 시도, 성공 여부 반환)
        boolean deleted = menuService.removeIngredientFromMenu(menuItemId, ingredientId);

        // 2. 응답 데이터 구성 (명세서 준수)
        Map<String, Object> data = new HashMap<>();
        data.put("menuItemId", menuItemId);
        data.put("ingredientId", ingredientId);
        data.put("deleted", deleted);

        // 3. 최종 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

}
