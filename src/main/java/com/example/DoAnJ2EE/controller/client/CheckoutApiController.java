package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.CheckoutRequest;
import com.example.DoAnJ2EE.dto.response.CheckoutResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutApiController {

    private final OrderService orderService;

    @PostMapping("/cod")
    public ResponseEntity<CheckoutResponse> checkoutCod(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CheckoutRequest request
    ) {
        return ResponseEntity.ok(orderService.checkoutCod(userDetails.getUser(), request));
    }
}