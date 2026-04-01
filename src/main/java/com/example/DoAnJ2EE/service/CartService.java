package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.request.AddToCartRequest;
import com.example.DoAnJ2EE.dto.request.UpdateCartItemRequest;
import com.example.DoAnJ2EE.dto.response.CartResponse;
import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.User;

import java.util.Optional;

public interface CartService {
    Optional<Cart> findByUser(User user);
    Cart getOrCreateCart(User user);

    CartResponse getMyCart(User user);
    CartResponse addToCart(User user, AddToCartRequest request);
    CartResponse updateCartItem(User user, UpdateCartItemRequest request);
    CartResponse removeCartItem(User user, Long cartItemId);
}