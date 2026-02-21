package com.fusioncrew.aikiosk.domain.ai.service;

import com.fusioncrew.aikiosk.domain.ai.dto.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private RestClient restClient;

    @Value("${ai.server.url:http://localhost:8000}")
    private String aiServerUrl;

    @PostConstruct
    public void init() {
        this.restClient = RestClient.builder()
                .baseUrl(aiServerUrl)
                .build();
    }

    private <T> AiCommonResponse<T> createErrorResponse() {
        return AiCommonResponse.<T>builder()
                .success(false)
                .timestamp(java.time.LocalDateTime.now().toString())
                .requestId("error-" + java.util.UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    public AiCommonResponse<SttDto.Result> requestStt(SttDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/stt")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<SttDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI STT request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<TtsDto.Result> requestTts(TtsDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/tts")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<TtsDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI TTS request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<LlmChatDto.Result> requestLlmChat(LlmChatDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/llm/chat")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<LlmChatDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI LLM Chat request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<NluParseDto.Result> requestNluParse(NluParseDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/nlu/parse")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<NluParseDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI NLU Parse request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<HesitationDto.Result> requestHesitation(HesitationDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/vision/hesitation")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<HesitationDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI Hesitation request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<FaceMeshDto.Result> requestFaceMesh(FaceMeshDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/vision/facemesh")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<FaceMeshDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI FaceMesh request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<PoseDto.Result> requestPose(PoseDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/vision/pose")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<PoseDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI Pose request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<HandsDto.Result> requestHands(HandsDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/vision/hands")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<HandsDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI Hands request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<SignLanguageDto.Result> requestSignLanguage(SignLanguageDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/vision/sign-language/interpret")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<SignLanguageDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI SignLanguage request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<LlmSuggestDto.Result> requestLlmSuggest(LlmSuggestDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/llm/suggest")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<LlmSuggestDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI LLM Suggest request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<LlmSummarizeDto.Result> requestLlmSummarize(LlmSummarizeDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v1/llm/summarize")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<LlmSummarizeDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI LLM Summarize request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<SttDto.Result> requestSttV2(SttDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/stt")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<SttDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI STT v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<TtsDto.Result> requestTtsV2(TtsDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/tts")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<TtsDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI TTS v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<LlmChatDto.Result> requestLlmChatV2(LlmChatDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/llm/chat")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<LlmChatDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI LLM Chat v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<NluParseDto.Result> requestNluParseV2(NluParseDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/nlu/parse")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<NluParseDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI NLU Parse v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<HesitationDto.Result> requestHesitationV2(HesitationDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/vision/hesitation")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<HesitationDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI Hesitation v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<FaceMeshDto.Result> requestFaceMeshV2(FaceMeshDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/vision/facemesh")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<FaceMeshDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI FaceMesh v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<PoseDto.Result> requestPoseV2(PoseDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/vision/pose")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<PoseDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI Pose v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<HandsDto.Result> requestHandsV2(HandsDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/vision/hands")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<HandsDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI Hands v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<SignLanguageDto.Result> requestSignLanguageV2(SignLanguageDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/vision/sign-language/interpret")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<SignLanguageDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI SignLanguage v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<LlmSuggestDto.Result> requestLlmSuggestV2(LlmSuggestDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/llm/suggest")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<LlmSuggestDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI LLM Suggest v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public AiCommonResponse<LlmSummarizeDto.Result> requestLlmSummarizeV2(LlmSummarizeDto.Request request) {
        try {
            return restClient.post()
                    .uri("/api/v2/llm/summarize")
                    .body(request)
                    .retrieve()
                    .body(new ParameterizedTypeReference<AiCommonResponse<LlmSummarizeDto.Result>>() {
                    });
        } catch (Exception e) {
            log.error("AI LLM Summarize v2 request failed: {}", e.getMessage());
            return createErrorResponse();
        }
    }

    public Object requestMetaHealth() {
        try {
            return restClient.get()
                    .uri("/api/v1/meta/health")
                    .retrieve()
                    .body(Object.class);
        } catch (Exception e) {
            log.error("AI Health Check failed: {}", e.getMessage());
            return null;
        }
    }

    public Object requestMetaHealthV2() {
        try {
            return restClient.get()
                    .uri("/api/v2/meta/health")
                    .retrieve()
                    .body(Object.class);
        } catch (Exception e) {
            log.error("AI v2 Health Check failed: {}", e.getMessage());
            return null;
        }
    }
}
