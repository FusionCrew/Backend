package com.fusioncrew.aikiosk.domain.menu.controller;

import com.fusioncrew.aikiosk.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/menu-ingredient-mappings")
@RequiredArgsConstructor
public class MenuIngredientMappingController {

    private final MenuService menuService;

    @GetMapping("/summary")
    public Map<String, Object> getMappingSummary() {
        List<Map<String, Object>> items = menuService.getMappingSummary();

        Map<String, Object> data = new HashMap<>();
        data.put("items", items);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }
}
