package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.AddToCartRequest;
import com.example.DoAnJ2EE.dto.request.UpdateCartItemRequest;
import com.example.DoAnJ2EE.dto.response.CartResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(cartService.getMyCart(userDetails.getUser()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AddToCartRequest request
    ) {
        return ResponseEntity.ok(cartService.addToCart(userDetails.getUser(), request));
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateCartItemRequest request
    ) {
        return ResponseEntity.ok(cartService.updateCartItem(userDetails.getUser(), request));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(cartService.removeCartItem(userDetails.getUser(), cartItemId));
    }
}