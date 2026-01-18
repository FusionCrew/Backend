package com.fusioncrew.aikiosk.domain.ingredient.init;

import com.fusioncrew.aikiosk.domain.ingredient.entity.AllergyTag;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IngredientDataInitializer implements CommandLineRunner {

    private final IngredientRepository ingredientRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ingredientRepository.count() == 0) {
            ingredientRepository.saveAll(List.of(
                    Ingredient.builder()
                            .ingredientId("ing_01")
                            .name("소고기 패티")
                            .allergyTag(AllergyTag.NONE)
                            .calories(250)
                            .protein(20)
                            .sodium(100)
                            .extraPrice(2000)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_02")
                            .name("치즈(체다)")
                            .allergyTag(AllergyTag.MILK)
                            .calories(100)
                            .protein(6)
                            .sodium(200)
                            .extraPrice(500)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_03")
                            .name("양상추")
                            .allergyTag(AllergyTag.NONE)
                            .calories(5)
                            .protein(0)
                            .sodium(5)
                            .extraPrice(0)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_04")
                            .name("토마토")
                            .allergyTag(AllergyTag.NONE)
                            .calories(10)
                            .protein(0)
                            .sodium(2)
                            .extraPrice(0)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_05")
                            .name("피클")
                            .allergyTag(AllergyTag.NONE)
                            .calories(10)
                            .protein(0)
                            .sodium(150)
                            .extraPrice(0)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_06")
                            .name("번(빵)")
                            .allergyTag(AllergyTag.WHEAT)
                            .calories(150)
                            .protein(4)
                            .sodium(250)
                            .extraPrice(0)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_07")
                            .name("베이컨")
                            .allergyTag(AllergyTag.NONE)
                            .calories(80)
                            .protein(5)
                            .sodium(300)
                            .extraPrice(1000)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_08")
                            .name("양파")
                            .allergyTag(AllergyTag.NONE)
                            .calories(15)
                            .protein(0)
                            .sodium(1)
                            .extraPrice(0)
                            .build(),
                    Ingredient.builder()
                            .ingredientId("ing_09")
                            .name("마요 소스")
                            .allergyTag(AllergyTag.EGG)
                            .calories(90)
                            .protein(0)
                            .sodium(80)
                            .extraPrice(0)
                            .build()));
            System.out.println("✅ 초기 재료 데이터가 생성되었습니다.");
        }
    }
}
