package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.AiChat.AiChatRequest;
import com.example.DoAnJ2EE.dto.AiChat.AiChatResponse;
import com.example.DoAnJ2EE.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatApiController {

    private final AiChatService aiChatService;

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        String answer = aiChatService.chat(
                request.getMessage(),
                request.getMotorbikeId(),
                request.getPageContext()
        );
        return ResponseEntity.ok(new AiChatResponse(answer));
    }
}