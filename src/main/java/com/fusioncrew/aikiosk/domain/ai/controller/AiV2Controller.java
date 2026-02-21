package com.fusioncrew.aikiosk.domain.ai.controller;

import com.fusioncrew.aikiosk.domain.ai.dto.*;
import com.fusioncrew.aikiosk.domain.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/ai")
@RequiredArgsConstructor
public class AiV2Controller {

    private final AiService aiService;

    @RequestMapping("/meta/health")
    public ResponseEntity<Object> metaHealth() {
        return ResponseEntity.ok(aiService.requestMetaHealthV2());
    }

    @PostMapping("/stt")
    public ResponseEntity<AiCommonResponse<SttDto.Result>> stt(@RequestBody SttDto.Request request) {
        return ResponseEntity.ok(aiService.requestSttV2(request));
    }

    @PostMapping("/tts")
    public ResponseEntity<AiCommonResponse<TtsDto.Result>> tts(@RequestBody TtsDto.Request request) {
        return ResponseEntity.ok(aiService.requestTtsV2(request));
    }

    @PostMapping("/llm/chat")
    public ResponseEntity<AiCommonResponse<LlmChatDto.Result>> llmChat(@RequestBody LlmChatDto.Request request) {
        return ResponseEntity.ok(aiService.requestLlmChatV2(request));
    }

    @PostMapping("/nlu/parse")
    public ResponseEntity<AiCommonResponse<NluParseDto.Result>> nluParse(@RequestBody NluParseDto.Request request) {
        return ResponseEntity.ok(aiService.requestNluParseV2(request));
    }

    @PostMapping("/vision/hesitation")
    public ResponseEntity<AiCommonResponse<HesitationDto.Result>> hesitation(
            @RequestBody HesitationDto.Request request) {
        return ResponseEntity.ok(aiService.requestHesitationV2(request));
    }

    @PostMapping("/vision/facemesh")
    public ResponseEntity<AiCommonResponse<FaceMeshDto.Result>> faceMesh(@RequestBody FaceMeshDto.Request request) {
        return ResponseEntity.ok(aiService.requestFaceMeshV2(request));
    }

    @PostMapping("/vision/pose")
    public ResponseEntity<AiCommonResponse<PoseDto.Result>> pose(@RequestBody PoseDto.Request request) {
        return ResponseEntity.ok(aiService.requestPoseV2(request));
    }

    @PostMapping("/vision/hands")
    public ResponseEntity<AiCommonResponse<HandsDto.Result>> hands(@RequestBody HandsDto.Request request) {
        return ResponseEntity.ok(aiService.requestHandsV2(request));
    }

    @PostMapping("/vision/sign-language/interpret")
    public ResponseEntity<AiCommonResponse<SignLanguageDto.Result>> interpretSignLanguage(
            @RequestBody SignLanguageDto.Request request) {
        return ResponseEntity.ok(aiService.requestSignLanguageV2(request));
    }

    @PostMapping("/llm/suggest")
    public ResponseEntity<AiCommonResponse<LlmSuggestDto.Result>> suggest(@RequestBody LlmSuggestDto.Request request) {
        return ResponseEntity.ok(aiService.requestLlmSuggestV2(request));
    }

    @PostMapping("/llm/summarize")
    public ResponseEntity<AiCommonResponse<LlmSummarizeDto.Result>> summarize(
            @RequestBody LlmSummarizeDto.Request request) {
        return ResponseEntity.ok(aiService.requestLlmSummarizeV2(request));
    }

    @PostMapping("/warmup")
    public ResponseEntity<Void> warmup(@RequestBody java.util.Map<String, Object> request) {
        return ResponseEntity.ok().build();
    }
}
