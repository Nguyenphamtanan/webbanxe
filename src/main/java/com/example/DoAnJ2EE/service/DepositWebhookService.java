package com.example.DoAnJ2EE.service;

import java.util.Map;

public interface DepositWebhookService {
    void handleWebhook(Map<String, Object> payload);
}