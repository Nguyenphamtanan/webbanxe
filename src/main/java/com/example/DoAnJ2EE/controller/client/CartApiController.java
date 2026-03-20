package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.cart.AddToCartRequest;
import com.example.DoAnJ2EE.dto.cart.UpdateCartItemRequest;
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
    public ResponseEntity<?> getMyCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cartService.getMyCart(userDetails.getUser()));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(
                cartService.addToCart(
                        userDetails.getUser(),
                        request.getMotorbikeId(),
                        request.getQuantity()
                )
        );
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long id,
                                            @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(
                cartService.updateCartItem(
                        userDetails.getUser(),
                        id,
                        request.getQuantity()
                )
        );
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> removeCartItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long id) {
        cartService.removeCartItem(userDetails.getUser(), id);
        return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ thành công");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.clearCart(userDetails.getUser());
        return ResponseEntity.ok("Đã xóa toàn bộ giỏ hàng");
    }
}