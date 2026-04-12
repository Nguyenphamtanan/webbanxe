package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.service.AiChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-3-flash-preview}")
    private String model;

    @Override
    public String ask(String userMessage) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Thiếu GEMINI_API_KEY");
        }
        if (userMessage == null || userMessage.trim().isEmpty()) {
            throw new RuntimeException("Tin nhắn không được để trống");
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model + ":generateContent?key=" + apiKey;

        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text",
                                                "Bạn là trợ lý cho website bán xe máy. "
                                                        + "Trả lời ngắn gọn, đúng trọng tâm, bằng tiếng Việt. "
                                                        + "Câu hỏi người dùng: " + userMessage)
                                }
                        )
                }
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Không gọi được Gemini API");
        }

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode textNode = root.path("candidates").path(0)
                    .path("content").path("parts").path(0).path("text");

            if (textNode.isMissingNode() || textNode.asText().isBlank()) {
                throw new RuntimeException("Gemini không trả về nội dung");
            }

            return textNode.asText();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc phản hồi Gemini: " + e.getMessage(), e);
        }
    }
}