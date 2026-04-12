package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.AiChatRequest;
import com.example.DoAnJ2EE.dto.response.AiChatResponse;
import com.example.DoAnJ2EE.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
public class AiChatApiController {

    private final AiChatService aiChatService;

    @PostMapping
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        String reply = aiChatService.ask(request.getMessage());
        return ResponseEntity.ok(new AiChatResponse(reply));
    }
}