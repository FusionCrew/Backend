package com.fusioncrew.aikiosk.domain.menu.service;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuCreateRequestDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuItemResponseDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuSimpleResponseDto;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuDetailKioskResponseDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuUpdateRequestDto;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuIngredientLinkRequestDto;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

        private final MenuItemRepository menuRepository;
        private final IngredientRepository ingredientRepository;

        // 메뉴 목록 조회
        public List<MenuSimpleResponseDto> getAdminMenuList() {
                return menuRepository.findAllByOrderByMenuItemIdAsc().stream()
                                .map(MenuSimpleResponseDto::new)
                                .collect(Collectors.toList());
        }

        // [New] 메뉴 등록
        @Transactional
        public String createMenu(MenuCreateRequestDto requestDto) {
                String generatedId = "menu_" + UUID.randomUUID().toString().substring(0, 8);
                while (menuRepository.existsByMenuItemId(generatedId)) {
                        generatedId = "menu_" + UUID.randomUUID().toString().substring(0, 8);
                }

                MenuItem menuItem = requestDto.toEntity(generatedId);

                if (requestDto.getOptionGroups() != null) {
                        for (MenuCreateRequestDto.OptionGroupData groupData : requestDto.getOptionGroups()) {
                                com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup group = com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup
                                                .builder()
                                                .name(groupData.getName())
                                                .isRequired(groupData.isRequired())
                                                .isMultipleSelectionAllowed(groupData.isMultipleSelectionAllowed())
                                                .build();

                                if (groupData.getItems() != null) {
                                        for (MenuCreateRequestDto.OptionItemData itemData : groupData.getItems()) {
                                                com.fusioncrew.aikiosk.domain.menu.entity.OptionItem item = com.fusioncrew.aikiosk.domain.menu.entity.OptionItem
                                                                .builder()
                                                                .name(itemData.getName())
                                                                .extraPrice(itemData.getExtraPrice())
                                                                .build();
                                                group.addOptionItem(item);
                                        }
                                }
                                menuItem.addOptionGroup(group);
                        }
                }
                MenuItem saved = menuRepository.save(menuItem);
                return saved.getMenuItemId();
        }

        // [New] 메뉴 수정 (PATCH)
        @Transactional
        public MenuItem updateMenu(String menuItemId, MenuUpdateRequestDto requestDto) {
                MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

                menu.updateDetails(
                                requestDto.getPrice(),
                                requestDto.getImageUrl(),
                                requestDto.getHidden(),
                                requestDto.getDescription(),
                                requestDto.getName(),
                                requestDto.getNameEn(),
                                requestDto.getCategoryId());

                if (requestDto.getOptionGroups() != null) {
                        menu.clearOptionGroups();
                        for (MenuCreateRequestDto.OptionGroupData groupData : requestDto.getOptionGroups()) {
                                com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup group = com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup
                                                .builder()
                                                .name(groupData.getName())
                                                .isRequired(groupData.isRequired())
                                                .isMultipleSelectionAllowed(groupData.isMultipleSelectionAllowed())
                                                .build();

                                if (groupData.getItems() != null) {
                                        for (MenuCreateRequestDto.OptionItemData itemData : groupData.getItems()) {
                                                com.fusioncrew.aikiosk.domain.menu.entity.OptionItem item = com.fusioncrew.aikiosk.domain.menu.entity.OptionItem
                                                                .builder()
                                                                .name(itemData.getName())
                                                                .extraPrice(itemData.getExtraPrice())
                                                                .build();
                                                group.addOptionItem(item);
                                        }
                                }
                                menu.addOptionGroup(group);
                        }
                }

                return menu;
        }

        // [New] 메뉴-재료 매핑 (덮어쓰기 방식)
        @Transactional
        public List<String> updateMenuIngredients(String menuItemId, MenuIngredientLinkRequestDto requestDto) {
                MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

                List<String> requestedIds = requestDto.getIngredientIds();
                if (requestedIds == null || requestedIds.isEmpty()) {
                        menu.updateIngredients(new ArrayList<>());
                        return new ArrayList<>();
                }

                Set<String> uniqueIds = new HashSet<>(requestedIds);
                List<Ingredient> foundIngredients = ingredientRepository
                                .findAllByIngredientIdIn(new ArrayList<>(uniqueIds));

                if (foundIngredients.size() != uniqueIds.size()) {
                        throw new IllegalArgumentException("요청한 재료 중 존재하지 않는 ID가 포함되어 있습니다.");
                }

                menu.updateIngredients(foundIngredients);
                return foundIngredients.stream()
                                .map(Ingredient::getIngredientId)
                                .collect(Collectors.toList());
        }

        // [New] 키오스크용 메뉴 상세 조회
        public MenuDetailKioskResponseDto getKioskMenuDetail(String menuItemId) {
                MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));

                List<MenuDetailKioskResponseDto.OptionGroup> options;

                if (menu.getOptionGroups() != null && !menu.getOptionGroups().isEmpty()) {
                        options = menu.getOptionGroups().stream()
                                        .map(group -> MenuDetailKioskResponseDto.OptionGroup.builder()
                                                        .optionGroup(group.getName())
                                                        .isRequired(group.isRequired())
                                                        .isMultipleSelectionAllowed(group.isMultipleSelectionAllowed())
                                                        .choices(group.getOptionItems().stream()
                                                                        .map(item -> MenuDetailKioskResponseDto.OptionChoice
                                                                                        .builder()
                                                                                        .name(item.getName())
                                                                                        .extraPrice(item.getExtraPrice())
                                                                                        .build())
                                                                        .collect(Collectors.toList()))
                                                        .build())
                                        .collect(Collectors.toList());
                } else {
                        // Default options for backward compatibility
                        options = Arrays.asList(
                                        MenuDetailKioskResponseDto.OptionGroup.builder()
                                                        .optionGroup("SIZE")
                                                        .isRequired(true)
                                                        .isMultipleSelectionAllowed(false)
                                                        .choices(Arrays.asList(
                                                                        MenuDetailKioskResponseDto.OptionChoice
                                                                                        .builder().name("SINGLE")
                                                                                        .extraPrice(0).build(),
                                                                        MenuDetailKioskResponseDto.OptionChoice
                                                                                        .builder().name("DOUBLE")
                                                                                        .extraPrice(2000).build()))
                                                        .build());
                }

                return MenuDetailKioskResponseDto.fromEntity(menu, options);
        }

        public List<MenuSimpleResponseDto> getKioskMenuList() {
                return menuRepository.findAllByOrderByMenuItemIdAsc().stream()
                                .filter(item -> !item.isHidden())
                                .map(MenuSimpleResponseDto::new)
                                .collect(Collectors.toList());
        }

        public MenuItemResponseDto getMenuDetail(String menuItemId) {
                MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
                return new MenuItemResponseDto(menu);
        }

        public MenuItem getMenuEntity(String menuItemId) {
                return menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
        }

        @Transactional
        public void deleteMenu(String menuItemId) {
                MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
                menuRepository.delete(menu);
        }

        @Transactional
        public boolean removeIngredientFromMenu(String menuItemId, String ingredientId) {
                MenuItem menu = menuRepository.findByMenuItemId(menuItemId)
                                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다: " + menuItemId));
                return menu.removeIngredientById(ingredientId);
        }

        public List<Map<String, Object>> getMappingSummary() {
                return menuRepository.findAllByOrderByMenuItemIdAsc().stream()
                                .map(menu -> {
                                        Map<String, Object> summary = new HashMap<>();
                                        summary.put("menuItemId", menu.getMenuItemId());
                                        summary.put("name", menu.getName());
                                        summary.put("ingredientCount", menu.getIngredients().size());
                                        summary.put("isMapped", !menu.getIngredients().isEmpty());
                                        return summary;
                                })
                                .collect(Collectors.toList());
        }
}
