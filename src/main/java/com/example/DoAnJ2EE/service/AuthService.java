package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.dto.auth.AuthResponse;
import com.example.DoAnJ2EE.dto.auth.LoginRequest;
import com.example.DoAnJ2EE.dto.auth.RefreshTokenRequest;
import com.example.DoAnJ2EE.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(String refreshToken);
}