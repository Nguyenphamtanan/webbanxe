package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.entity.RefreshToken;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.RefreshTokenRepository;
import com.example.DoAnJ2EE.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .isRevoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public boolean isValid(RefreshToken refreshToken) {
        return !Boolean.TRUE.equals(refreshToken.getIsRevoked())
                && refreshToken.getExpiryAt().isAfter(LocalDateTime.now());
    }

    @Override
    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setIsRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }
}