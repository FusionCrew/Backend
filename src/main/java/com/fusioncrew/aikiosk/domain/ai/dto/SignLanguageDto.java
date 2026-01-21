package com.fusioncrew.aikiosk.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

public class SignLanguageDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private List<String> framesBase64;
        private String sessionId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private List<Command> commands;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Command {
        private String type; // e.g., "ADD_ITEM"
        private String menuName;
        private Integer quantity;
        private Double confidence;
    }
}
