package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.CreateDepositRequest;
import com.example.DoAnJ2EE.dto.response.DepositResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.DoAnJ2EE.dto.request.RefundDepositRequest;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositApiController {

    private final DepositService depositService;

    @PostMapping
    public ResponseEntity<DepositResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateDepositRequest request
    ) {
        return ResponseEntity.ok(depositService.create(userDetails.getUser(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepositResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(depositService.getById(id, userDetails.getUser()));
    }

    @GetMapping("/my")
    public ResponseEntity<List<DepositResponse>> myDeposits(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(depositService.getMyDeposits(userDetails.getUser()));
    }

    @PutMapping("/{id}/refund-paid")
    public ResponseEntity<?> refundPaidDeposit(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RefundDepositRequest request
    ) {
        depositService.refundPaidDeposit(id, request, userDetails.getUser());
        return ResponseEntity.ok().body(java.util.Map.of(
                "message", "Đã gửi yêu cầu hoàn tiền"
        ));
    }
}