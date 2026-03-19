package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // ===== Auth =====
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);

    // ===== Admin =====
    List<User> findAll();
    User findById(Long id);
    User updateStatus(Long id, Boolean isActive);
    User updateRole(Long id, String role);
}