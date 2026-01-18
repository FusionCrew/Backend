package com.fusioncrew.aikiosk.domain.meta.controller;

import com.fusioncrew.aikiosk.domain.meta.dto.HealthResponse;
import com.fusioncrew.aikiosk.domain.meta.entity.HealthStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meta")
public class MetaHealthController {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.service-name:spring-api}")
    private String serviceName;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        HealthResponse response = HealthResponse.builder()
                .success(true)
                .data(HealthResponse.HealthData.builder()
                        .status(HealthStatus.UP)
                        .service(serviceName)
                        .version(appVersion)
                        .build())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.ok(response);
    }
}
