package com.fusioncrew.aikiosk.domain.kiosk.service;

import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskMenuListResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskIngredientResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskRecommendationResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskCategoryResponse;
import com.fusioncrew.aikiosk.domain.ingredient.entity.AllergyTag;
import com.fusioncrew.aikiosk.domain.kiosk.entity.RecommendationBasis;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class KioskMenuService {

        private final MenuItemRepository menuItemRepository;
        private final IngredientRepository ingredientRepository;

        public KioskMenuListResponse getKioskMenuItems(String categoryId, String keyword, String cursor, int size) {
                log.debug("Fetching kiosk menu items: categoryId={}, keyword={}, cursor={}, size={}",
                                categoryId, keyword, cursor, size);

                // Fetch size + 1 to check if there's a next page
                List<MenuItem> items = menuItemRepository.findKioskMenuItems(
                                categoryId, keyword, cursor, PageRequest.of(0, size + 1));

                boolean hasNext = items.size() > size;
                String nextCursor = null;

                if (hasNext) {
                        items = items.subList(0, size);
                        nextCursor = items.get(items.size() - 1).getMenuItemId();
                }

                List<KioskMenuListResponse.KioskMenuItemDto> dtos = items.stream()
                                .map(item -> KioskMenuListResponse.KioskMenuItemDto.builder()
                                                .menuItemId(item.getMenuItemId())
                                                .name(item.getName())
                                                .price(item.getPrice())
                                                .thumbnailUrl(item.getImageUrl())
                                                .isAvailable(true) // TODO: Implement availability logic if needed
                                                .categoryId(item.getCategoryId())
                                                .build())
                                .collect(Collectors.toList());

                return KioskMenuListResponse.builder()
                                .success(true)
                                .data(KioskMenuListResponse.MenuListData.builder()
                                                .items(dtos)
                                                .page(KioskMenuListResponse.PageInfo.builder()
                                                                .size(size)
                                                                .nextCursor(nextCursor)
                                                                .build())
                                                .build())
                                .build();
        }

        public KioskCategoryResponse getCategories() {
                log.debug("Fetching kiosk categories");

                // Mock mapping since we don't have a Category entity yet
                List<KioskCategoryResponse.CategoryDto> categories = List.of(
                                KioskCategoryResponse.CategoryDto.builder()
                                                .categoryId("cat_01")
                                                .name("커피")
                                                .build(),
                                KioskCategoryResponse.CategoryDto.builder()
                                                .categoryId("cat_02")
                                                .name("버거")
                                                .build(),
                                KioskCategoryResponse.CategoryDto.builder()
                                                .categoryId("cat_03")
                                                .name("사이드/음료")
                                                .build());

                return KioskCategoryResponse.builder()
                                .success(true)
                                .data(KioskCategoryResponse.CategoryListData.builder()
                                                .categories(categories)
                                                .build())
                                .build();
        }

        public KioskIngredientResponse getIngredientFilters() {
                log.debug("Fetching kiosk ingredient filters");

                List<KioskIngredientResponse.IngredientDto> ingredients = ingredientRepository.findAll().stream()
                                .map(ing -> KioskIngredientResponse.IngredientDto.builder()
                                                .ingredientId(ing.getIngredientId())
                                                .name(ing.getName())
                                                .build())
                                .collect(Collectors.toList());

                List<AllergyTag> allergyTags = Arrays.stream(AllergyTag.values())
                                .filter(tag -> tag != AllergyTag.NONE)
                                .collect(Collectors.toList());

                return KioskIngredientResponse.builder()
                                .success(true)
                                .data(KioskIngredientResponse.IngredientFilterData.builder()
                                                .ingredients(ingredients)
                                                .allergyTags(allergyTags)
                                                .build())
                                .build();
        }

        public KioskRecommendationResponse getRecommendations(String sessionId) {
                log.debug("Fetching kiosk recommendations for session: {}", sessionId);

                // Mock data based on specification
                List<KioskRecommendationResponse.RecommendationItemDto> items = List.of(
                                KioskRecommendationResponse.RecommendationItemDto.builder()
                                                .menuItemId("menu_010")
                                                .name("바질 치킨버거")
                                                .reason("처음 방문하신 손님께 가장 무난하고 만족도가 높은 메뉴라 추천드려요")
                                                .build(),
                                KioskRecommendationResponse.RecommendationItemDto.builder()
                                                .menuItemId("menu_004")
                                                .name("트러플 더블치즈버거")
                                                .reason("치즈 풍미를 좋아하시는 분들께 매장에서 가장 반응이 좋은 메뉴예요")
                                                .build(),
                                KioskRecommendationResponse.RecommendationItemDto.builder()
                                                .menuItemId("menu_007")
                                                .name("스파이시 할라피뇨버거")
                                                .reason("살짝 매콤한 맛을 찾으시는 손님들이 많이 선택하는 인기 메뉴예요")
                                                .build());

                return KioskRecommendationResponse.builder()
                                .success(true)
                                .data(KioskRecommendationResponse.RecommendationData.builder()
                                                .basis(RecommendationBasis.STORE_MANAGER_RECOMMENDATION)
                                                .items(items)
                                                .build())
                                .build();
        }
}
