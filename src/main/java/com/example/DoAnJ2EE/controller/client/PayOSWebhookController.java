package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.service.DepositWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payos")
@RequiredArgsConstructor
public class PayOSWebhookController {

    private final DepositWebhookService depositWebhookService;

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody Map<String, Object> payload) {
        System.out.println("=== WEBHOOK HIT ===");
        System.out.println("payload = " + payload);

        depositWebhookService.handleWebhook(payload);

        return ResponseEntity.ok(Map.of("success", true));
    }
}