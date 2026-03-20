package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.cart.CartResponse;
import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.User;

import java.util.Optional;

public interface CartService {
    Optional<Cart> findByUser(User user);
    Cart getOrCreateCart(User user);

    CartResponse getMyCart(User user);
    CartResponse addToCart(User user, Long motorbikeId, Integer quantity);
    CartResponse updateCartItem(User user, Long cartItemId, Integer quantity);
    void removeCartItem(User user, Long cartItemId);
    void clearCart(User user);
}