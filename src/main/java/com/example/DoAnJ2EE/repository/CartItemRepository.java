package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.CartItem;
import com.example.DoAnJ2EE.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndMotorbike(Cart cart, Motorbike motorbike);
    void deleteByCart(Cart cart);
}