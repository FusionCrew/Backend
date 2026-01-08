package com.fusioncrew.aikiosk.controller;

import com.fusioncrew.aikiosk.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("service", "AI Kiosk Backend");
        data.put("version", "1.0.0");

        return ResponseEntity.ok(ApiResponse.success("서버가 정상 작동 중입니다.", data));
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.success("테스트 성공!"));
    }
}
