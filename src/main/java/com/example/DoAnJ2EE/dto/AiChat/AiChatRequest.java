package com.example.DoAnJ2EE.dto.AiChat;

import lombok.Data;

@Data
public class AiChatRequest {
    private String message;
    private Long motorbikeId;
    private String pageContext;
}