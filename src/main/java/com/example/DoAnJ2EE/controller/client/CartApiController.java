package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.AddToCartRequest;
import com.example.DoAnJ2EE.dto.request.UpdateCartItemRequest;
import com.example.DoAnJ2EE.dto.response.CartResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getMyCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập để xem giỏ hàng"));
        }

        return ResponseEntity.ok(cartService.getMyCart(userDetails.getUser()));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AddToCartRequest request
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập để thêm vào giỏ hàng"));
        }

        return ResponseEntity.ok(cartService.addToCart(userDetails.getUser(), request));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateCartItemRequest request
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập để cập nhật giỏ hàng"));
        }

        return ResponseEntity.ok(cartService.updateCartItem(userDetails.getUser(), request));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập để xóa sản phẩm khỏi giỏ hàng"));
        }

        return ResponseEntity.ok(cartService.removeCartItem(userDetails.getUser(), cartItemId));
    }
}