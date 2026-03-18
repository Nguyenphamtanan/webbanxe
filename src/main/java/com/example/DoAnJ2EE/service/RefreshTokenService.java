package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.RefreshToken;
import com.example.DoAnJ2EE.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    boolean isValid(RefreshToken refreshToken);
    void revokeToken(String token);
}