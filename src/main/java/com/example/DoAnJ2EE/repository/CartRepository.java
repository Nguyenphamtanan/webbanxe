package com.example.DoAnJ2EE.repository;

import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}