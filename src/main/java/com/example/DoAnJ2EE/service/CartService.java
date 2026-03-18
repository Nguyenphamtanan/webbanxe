package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.User;

import java.util.Optional;

public interface CartService {
    Optional<Cart> findByUser(User user);
    Cart getOrCreateCart(User user);
}