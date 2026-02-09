package com.fusioncrew.aikiosk.domain.menu.init;

import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuDataInitializer implements CommandLineRunner {

        private final MenuItemRepository menuItemRepository;
        private final JdbcTemplate jdbcTemplate;

        @Override
        public void run(String... args) {
                // [Force Migration] 이미지 주소 컬럼 타입을 TEXT로 변경 (Hibernate update가 실패할 경우 대비)
                try {
                        System.out.println("🔄 DB 스키마 마이그레이션 시도 (image_url -> TEXT)...");
                        jdbcTemplate.execute("ALTER TABLE menu_items ALTER COLUMN image_url TYPE TEXT");
                        String dataType = jdbcTemplate.queryForObject(
                                        "SELECT data_type FROM information_schema.columns WHERE table_name = 'menu_items' AND column_name = 'image_url'",
                                        String.class);
                        System.out.println("✅ DB 스키마 마이그레이션 성공! 현재 타입: " + dataType);
                } catch (Exception e) {
                        System.out.println("ℹ️ DB 스키마 마이그레이션 건너뜀 (이미 반영되었거나 다른 오류): " + e.getMessage());
                }
                try {
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
                } catch (Exception e) {
                        System.err.println("❌ 메뉴 데이터 초기화 실패 (무시하고 계속 진행): " + e.getMessage());
                }
        }
}
