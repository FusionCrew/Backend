package com.fusioncrew.aikiosk.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

public class LlmChatDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private List<Message> messages;
        private String sessionId;
        private String orderType;
        private Context context;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Context {
        private String sessionId;
        private String kioskState;
        private Map<String, Object> state;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        private String reply;
        private String text;
        private String intent;
        private String action;
        private Map<String, Object> actionData;
        private String orchestrator;
        private String generatedAt;
        private Map<String, Object> live2d;
        private Map<String, Object> parallel;
        private String stage;

        // Backward-compatible fields
        private String assistantMessage;
        private String intentHint;
    }
}
