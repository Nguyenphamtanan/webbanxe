package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.CartRepository;
import com.example.DoAnJ2EE.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Optional<Cart> findByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }
}