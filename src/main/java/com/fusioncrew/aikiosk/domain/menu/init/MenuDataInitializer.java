package com.fusioncrew.aikiosk.domain.menu.init;

import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
@RequiredArgsConstructor
public class MenuDataInitializer implements CommandLineRunner {

        private final MenuItemRepository menuItemRepository;
        private final com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository ingredientRepository;
        private final JdbcTemplate jdbcTemplate;

        @Override
        @Transactional
        public void run(String... args) {
                // [Force Migration] 이미지 주소 컬럼 타입을 TEXT로 변경
                try {
                        jdbcTemplate.execute("ALTER TABLE menu_items ALTER COLUMN image_url TYPE TEXT");
                } catch (Exception e) {
                        // ignore
                }

                try {
                        if (menuItemRepository.count() == 0) {
                                // 1. 재료 준비 (IngredientDataInitializer에서 생성된 것 활용)
                                var patty = ingredientRepository.findByIngredientId("ing_01").orElse(null);
                                var cheese = ingredientRepository.findByIngredientId("ing_02").orElse(null);
                                var lettuce = ingredientRepository.findByIngredientId("ing_03").orElse(null);
                                var tomato = ingredientRepository.findByIngredientId("ing_04").orElse(null);
                                var pickle = ingredientRepository.findByIngredientId("ing_05").orElse(null);
                                var bacon = ingredientRepository.findByIngredientId("ing_07").orElse(null);
                                var onion = ingredientRepository.findByIngredientId("ing_08").orElse(null);

                                // 2. 메뉴 생성
                                MenuItem bulgogi = MenuItem.builder()
                                                .menuItemId("menu_003")
                                                .name("불고기버거")
                                                .nameEn("Bulgogi Burger")
                                                .price(5500)
                                                .hidden(false)
                                                .categoryId("cat_burger")
                                                .imageUrl("https://cdn.example.com/bulgogi_burger.png")
                                                .description("달콤한 불고기 소스")
                                                .build();

                                if (patty != null) bulgogi.getIngredients().add(patty);
                                if (lettuce != null) bulgogi.getIngredients().add(lettuce);
                                if (onion != null) bulgogi.getIngredients().add(onion);

                                // 옵션 그룹 추가
                                com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup toppings = com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup.builder()
                                                .name("토핑 추가")
                                                .isRequired(false)
                                                .isMultipleSelectionAllowed(true)
                                                .build();
                                toppings.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("베이컨 추가").extraPrice(1000).build());
                                toppings.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("치즈 추가").extraPrice(500).build());
                                toppings.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("패티 추가").extraPrice(2000).build());
                                
                                bulgogi.addOptionGroup(toppings);

                                MenuItem cheeseBurger = MenuItem.builder()
                                                .menuItemId("menu_004")
                                                .name("치즈버거")
                                                .nameEn("Cheese Burger")
                                                .price(6000)
                                                .hidden(false)
                                                .categoryId("cat_burger")
                                                .imageUrl("https://cdn.example.com/cheese_burger.png")
                                                .description("진한 체다 치즈")
                                                .build();

                                if (patty != null) cheeseBurger.getIngredients().add(patty);
                                if (cheese != null) cheeseBurger.getIngredients().add(cheese);
                                if (pickle != null) cheeseBurger.getIngredients().add(pickle);

                                com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup cheeseOptions = com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup.builder()
                                                .name("치즈 선택")
                                                .isRequired(true)
                                                .isMultipleSelectionAllowed(false)
                                                .build();
                                cheeseOptions.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("체다 치즈").extraPrice(0).build());
                                cheeseOptions.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("모짜렐라 치즈").extraPrice(500).build());
                                
                                cheeseBurger.addOptionGroup(cheeseOptions);

                                menuItemRepository.saveAll(List.of(
                                                MenuItem.builder().menuItemId("menu_001").name("카페라떼").nameEn("Latte").price(4500)
                                                                .hidden(false).categoryId("cat_drink").imageUrl("https://cdn.example.com/latte.png").description("부드러운 우유와 에스프레소").build(),
                                                MenuItem.builder().menuItemId("menu_002").name("아메리카노").nameEn("Americano").price(3000)
                                                                .hidden(false).categoryId("cat_drink").imageUrl("https://cdn.example.com/americano.png").description("깔끔한 에스프레소와 물").build(),
                                                bulgogi,
                                                cheeseBurger
                                ));
                                System.out.println("✅ 초기 메뉴 및 옵션 데이터가 생성되었습니다.");
                        } else {
                                // 기존 데이터가 있는 경우 버거 메뉴들에 옵션이 채워졌는지 확인 후 업데이트
                                var patty = ingredientRepository.findByIngredientId("ing_01").orElse(null);
                                var cheese = ingredientRepository.findByIngredientId("ing_02").orElse(null);
                                var lettuce = ingredientRepository.findByIngredientId("ing_03").orElse(null);
                                var onion = ingredientRepository.findByIngredientId("ing_08").orElse(null);

                                menuItemRepository.findByMenuItemId("menu_003").ifPresent(bulgogi -> {
                                        // 카테고리 ID 보정
                                        if ("cat_02".equals(bulgogi.getCategoryId())) {
                                            bulgogi.updateDetails(null, null, null, null, null, null, "cat_burger");
                                        }
                                        if (bulgogi.getOptionGroups().isEmpty()) {
                                                if (bulgogi.getIngredients().isEmpty()) {
                                                        if (patty != null) bulgogi.getIngredients().add(patty);
                                                        if (lettuce != null) bulgogi.getIngredients().add(lettuce);
                                                        if (onion != null) bulgogi.getIngredients().add(onion);
                                                }
                                                com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup toppings = com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup.builder()
                                                                .name("토핑 추가")
                                                                .isRequired(false)
                                                                .isMultipleSelectionAllowed(true)
                                                                .build();
                                                toppings.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("베이컨 추가").extraPrice(1000).build());
                                                toppings.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("치즈 추가").extraPrice(500).build());
                                                toppings.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("패티 추가").extraPrice(2000).build());
                                                bulgogi.addOptionGroup(toppings);
                                                menuItemRepository.save(bulgogi);
                                                System.out.println("✅ 불고기버거 옵션이 업데이트되었습니다.");
                                        }
                                });

                                menuItemRepository.findByMenuItemId("menu_004").ifPresent(cheeseBurger -> {
                                        // 카테고리 ID 보정
                                        if ("cat_02".equals(cheeseBurger.getCategoryId())) {
                                            cheeseBurger.updateDetails(null, null, null, null, null, null, "cat_burger");
                                        }
                                        if (cheeseBurger.getOptionGroups().isEmpty()) {
                                                if (cheeseBurger.getIngredients().isEmpty()) {
                                                        if (patty != null) cheeseBurger.getIngredients().add(patty);
                                                        if (cheese != null) cheeseBurger.getIngredients().add(cheese);
                                                }
                                                com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup cheeseOptions = com.fusioncrew.aikiosk.domain.menu.entity.OptionGroup.builder()
                                                                .name("치즈 선택")
                                                                .isRequired(true)
                                                                .isMultipleSelectionAllowed(false)
                                                                .build();
                                                cheeseOptions.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("체다 치즈").extraPrice(0).build());
                                                cheeseOptions.addOptionItem(com.fusioncrew.aikiosk.domain.menu.entity.OptionItem.builder().name("모짜렐라 치즈").extraPrice(500).build());
                                                cheeseBurger.addOptionGroup(cheeseOptions);
                                                menuItemRepository.save(cheeseBurger);
                                                System.out.println("✅ 치즈버거 옵션이 업데이트되었습니다.");
                                        }
                                });

                                // 라떼, 아메리카노 카테고리 보정
                                menuItemRepository.findByMenuItemId("menu_001").ifPresent(i -> {
                                    if ("cat_01".equals(i.getCategoryId())) i.updateDetails(null, null, null, null, null, null, "cat_drink");
                                });
                                menuItemRepository.findByMenuItemId("menu_002").ifPresent(i -> {
                                    if ("cat_01".equals(i.getCategoryId())) i.updateDetails(null, null, null, null, null, null, "cat_drink");
                                });
                        }
                } catch (Exception e) {
                        System.err.println("❌ 메뉴 데이터 초기화 실패: " + e.getMessage());
                }
        }
}
