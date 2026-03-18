package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
}