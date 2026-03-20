package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.checkout.CheckoutRequest;
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
    public ResponseEntity<?> checkoutCod(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.checkoutCod(userDetails.getUser(), request));
    }
    @PostMapping("/paypal/create-order")
    public ResponseEntity<?> createPaypalOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.createPaypalOrder(userDetails.getUser(), request));
    }

    @PostMapping("/paypal/capture")
    public ResponseEntity<?> capturePaypalOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestParam Long orderId,
                                                @RequestParam String paypalOrderId) {
        return ResponseEntity.ok(
                orderService.capturePaypalOrder(
                        userDetails.getUser(),
                        orderId,
                        paypalOrderId
                )
        );
    }
}