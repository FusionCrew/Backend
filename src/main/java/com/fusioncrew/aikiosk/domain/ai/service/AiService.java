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

    @Value("${ai.server.url}")
    private String aiServerUrl;

    @PostConstruct
    public void init() {
        this.restClient = RestClient.builder()
                .baseUrl(aiServerUrl)
                .build();
    }

    public AiCommonResponse<SttDto.Result> requestStt(SttDto.Request request) {
        return restClient.post()
                .uri("/api/v1/stt")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<SttDto.Result>>() {
                });
    }

    public AiCommonResponse<TtsDto.Result> requestTts(TtsDto.Request request) {
        return restClient.post()
                .uri("/api/v1/tts")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<TtsDto.Result>>() {
                });
    }

    public AiCommonResponse<LlmChatDto.Result> requestLlmChat(LlmChatDto.Request request) {
        return restClient.post()
                .uri("/api/v1/llm/chat")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<LlmChatDto.Result>>() {
                });
    }

    public AiCommonResponse<NluParseDto.Result> requestNluParse(NluParseDto.Request request) {
        return restClient.post()
                .uri("/api/v1/nlu/parse")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<NluParseDto.Result>>() {
                });
    }

    public AiCommonResponse<HesitationDto.Result> requestHesitation(HesitationDto.Request request) {
        return restClient.post()
                .uri("/api/v1/vision/hesitation")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<HesitationDto.Result>>() {
                });
    }

    public AiCommonResponse<FaceMeshDto.Result> requestFaceMesh(FaceMeshDto.Request request) {
        return restClient.post()
                .uri("/api/v1/vision/facemesh")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<FaceMeshDto.Result>>() {
                });
    }

    public AiCommonResponse<PoseDto.Result> requestPose(PoseDto.Request request) {
        return restClient.post()
                .uri("/api/v1/vision/pose")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<PoseDto.Result>>() {
                });
    }

    public AiCommonResponse<HandsDto.Result> requestHands(HandsDto.Request request) {
        return restClient.post()
                .uri("/api/v1/vision/hands")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<HandsDto.Result>>() {
                });
    }

    public AiCommonResponse<SignLanguageDto.Result> requestSignLanguage(SignLanguageDto.Request request) {
        return restClient.post()
                .uri("/api/v1/vision/sign-language/interpret")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<SignLanguageDto.Result>>() {
                });
    }

    public AiCommonResponse<LlmSuggestDto.Result> requestLlmSuggest(LlmSuggestDto.Request request) {
        return restClient.post()
                .uri("/api/v1/llm/suggest")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<LlmSuggestDto.Result>>() {
                });
    }

    public AiCommonResponse<LlmSummarizeDto.Result> requestLlmSummarize(LlmSummarizeDto.Request request) {
        return restClient.post()
                .uri("/api/v1/llm/summarize")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<AiCommonResponse<LlmSummarizeDto.Result>>() {
                });
    }

    public Object requestMetaHealth() {
        return restClient.get()
                .uri("/api/v1/meta/health")
                .retrieve()
                .body(Object.class);
    }
}
