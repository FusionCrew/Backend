package com.fusioncrew.aikiosk.domain.menu.service;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuCreateRequestDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuItemResponseDto;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuIngredientLinkRequestDto;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuDetailKioskResponseDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuDetailKioskResponseDto.OptionGroup;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuUpdateRequestDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuItemRepository menuRepository;

    // 메뉴 목록 조회
    public List<MenuItemResponseDto> getAdminMenuList() {
        return menuRepository.findAllByOrderByMenuItemIdAsc().stream()
                .map(MenuItemResponseDto::new)
                .collect(Collectors.toList());
    }

    // [New] 메뉴 등록 (이름: createMenu / 로직: ID 중복 체크 포함)
    @Transactional
    public String createMenu(MenuCreateRequestDto requestDto) {
        // 1. ID 생성 로직 (예: menu_ + UUID 8자리)
        String generatedId = "menu_" + UUID.randomUUID().toString().substring(0, 8);

        // 2. 중복 체크 (혹시나 겹칠 경우 대비 - 안전 장치)
        while (menuRepository.existsByMenuItemId(generatedId)) {
            generatedId = "menu_" + UUID.randomUUID().toString().substring(0, 8);
        }

        // 3. Entity 변환 및 저장
        MenuItem menuItem = requestDto.toEntity(generatedId);
        MenuItem saved = menuRepository.save(menuItem);

        // 4. 생성된 ID 반환
        return saved.getMenuItemId();
    }

    // [New] 메뉴 수정 (PATCH)
    @Transactional
    public MenuItem updateMenu(String menuItemId, MenuUpdateRequestDto requestDto) {
        // 1. 메뉴 찾기
        MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

        // 2. 부분 업데이트 실행 (DTO의 필드가 null이 아닌 것만 반영됨)
        menu.updateDetails(
                requestDto.getPrice(),
                requestDto.getImageUrl(),
                requestDto.getHidden(),
                requestDto.getDescription(),
                requestDto.getName(),
                requestDto.getNameEn(),
                requestDto.getCategoryId());

        // 3. 수정된 엔티티 반환 (Controller에서 응답 구성용)
        return menu;
    }

    private final IngredientRepository ingredientRepository; // 주입 필요

    // [New] 메뉴-재료 매핑 (덮어쓰기 방식)
    @Transactional
    public List<String> updateMenuIngredients(String menuItemId, MenuIngredientLinkRequestDto requestDto) {
        // 1. 메뉴 조회
        MenuItem menu = menuRepository.findByMenuItemId(menuItemId) // Optional 처리 필요
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

        // 2. 요청된 재료 ID 목록 (중복 제거)
        List<String> requestedIds = requestDto.getIngredientIds();
        if (requestedIds == null || requestedIds.isEmpty()) {
            // 빈 리스트가 오면 "모든 재료 연결 해제"로 처리
            menu.updateIngredients(new ArrayList<>());
            return new ArrayList<>();
        }

        // 중복 제거를 위해 Set으로 변환했다가 다시 List로 (혹시 모를 중복 입력 방지)
        Set<String> uniqueIds = new HashSet<>(requestedIds);

        // 3. 재료 조회 (DB에 실제 존재하는 것만 가져옴)
        List<Ingredient> foundIngredients = ingredientRepository.findAllByIngredientIdIn(new ArrayList<>(uniqueIds));

        // 4. [검증] 요청한 개수와 DB에서 찾은 개수가 다르면? -> 없는 ID가 있다는 뜻!
        if (foundIngredients.size() != uniqueIds.size()) {
            throw new IllegalArgumentException("요청한 재료 중 존재하지 않는 ID가 포함되어 있습니다.");
        }

        // 5. 덮어쓰기 (Replace)
        menu.updateIngredients(foundIngredients);

        // 6. 매핑된 ID 목록 반환
        return foundIngredients.stream()
                .map(Ingredient::getIngredientId)
                .collect(Collectors.toList());
    }

    // [New] 키오스크용 메뉴 상세 조회
    public MenuDetailKioskResponseDto getKioskMenuDetail(String menuItemId) {
        // 1. 메뉴 조회 (재료 포함)
        MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

        // 2. 옵션 목록 구성
        // (현재 DB에 옵션 테이블이 없으므로, 명세서 예시와 같게 하드코딩해서 내려줍니다.)
        // 나중에 OptionEntity 등을 만들면 DB에서 조회하는 로직으로 바꾸면 됩니다.
        List<OptionGroup> options = List.of(
                OptionGroup.builder().optionGroup("SIZE").choices(List.of("SINGLE", "DOUBLE")).build(),
                OptionGroup.builder().optionGroup("SET").choices(List.of("SINGLE_ONLY", "SET_MENU")).build(),
                OptionGroup.builder().optionGroup("EXTRA").choices(List.of("CHEESE", "BACON", "PATTY")).build());

        // 3. DTO 변환 및 반환
        return MenuDetailKioskResponseDto.builder()
                .menuItem(menu)
                .options(options)
                .build();
    }

    // [New] 관리자용 단일 메뉴 조회
    public MenuItemResponseDto getMenuDetail(String menuItemId) {
        MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
        return new MenuItemResponseDto(menu);
    }

    // [New] MenuItem Entity 직접 반환 (Controller에서 재료 목록 접근용)
    public MenuItem getMenuEntity(String menuItemId) {
        return menuRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
    }

    // [New] 메뉴 삭제
    @Transactional
    public void deleteMenu(String menuItemId) {
        MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
        menuRepository.delete(menu);
    }

    // [New] 메뉴-재료 매핑 삭제
    @Transactional
    public boolean removeIngredientFromMenu(String menuItemId, String ingredientId) {
        MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

        // 재료 제거 시도 (성공 시 true 반환)
        return menu.removeIngredientById(ingredientId);
    }

    // [New] 전체 메뉴-재료 매핑 요약 조회
    public List<java.util.Map<String, Object>> getMappingSummary() {
        return menuRepository.findAllByOrderByMenuItemIdAsc().stream()
                .map(menu -> {
                    java.util.Map<String, Object> summary = new java.util.HashMap<>();
                    summary.put("menuItemId", menu.getMenuItemId());
                    summary.put("name", menu.getName());
                    summary.put("ingredientCount", menu.getIngredients().size());
                    summary.put("isMapped", !menu.getIngredients().isEmpty());
                    return summary;
                })
                .collect(Collectors.toList());
    }
}
