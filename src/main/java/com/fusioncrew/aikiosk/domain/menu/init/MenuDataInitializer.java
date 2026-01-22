package com.fusioncrew.aikiosk.domain.menu.init;

import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuDataInitializer implements CommandLineRunner {

        private final MenuItemRepository menuItemRepository;

        @Override
        public void run(String... args) throws Exception {
                if (menuItemRepository.count() == 0) {
                        menuItemRepository.saveAll(List.of(
                                        MenuItem.builder()
                                                        .menuItemId("menu_001")
                                                        .name("카페라떼")
                                                        .nameEn("Latte")
                                                        .price(4500)
                                                        .hidden(false)
                                                        .categoryId("cat_01")
                                                        .imageUrl("https://cdn.example.com/latte.png")
                                                        .description("부드러운 우유와 에스프레소")
                                                        .build(),
                                        MenuItem.builder()
                                                        .menuItemId("menu_002")
                                                        .name("아메리카노")
                                                        .nameEn("Americano")
                                                        .price(3000)
                                                        .hidden(false)
                                                        .categoryId("cat_01")
                                                        .imageUrl("https://cdn.example.com/americano.png")
                                                        .description("깔끔한 에스프레소와 물")
                                                        .build(),
                                        MenuItem.builder()
                                                        .menuItemId("menu_003")
                                                        .name("불고기버거")
                                                        .nameEn("Bulgogi Burger")
                                                        .price(5500)
                                                        .hidden(false)
                                                        .categoryId("cat_02")
                                                        .imageUrl("https://cdn.example.com/bulgogi_burger.png")
                                                        .description("달콤한 불고기 소스")
                                                        .build(),
                                        MenuItem.builder()
                                                        .menuItemId("menu_004")
                                                        .name("치즈버거")
                                                        .nameEn("Cheese Burger")
                                                        .price(6000)
                                                        .hidden(false)
                                                        .categoryId("cat_02")
                                                        .imageUrl("https://cdn.example.com/cheese_burger.png")
                                                        .description("진한 체다 치즈")
                                                        .build(),
                                        MenuItem.builder()
                                                        .menuItemId("menu_005")
                                                        .name("비밀메뉴")
                                                        .nameEn("Secret Menu")
                                                        .price(10000)
                                                        .hidden(true)
                                                        .categoryId("cat_01")
                                                        .imageUrl("https://cdn.example.com/secret.png")
                                                        .description("숨겨진 메뉴")
                                                        .build()));
                        System.out.println("✅ 초기 메뉴 데이터가 생성되었습니다.");
                }
        }
}
