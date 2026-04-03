package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.UserRole;
import com.example.DoAnJ2EE.dto.auth.AuthResponse;
import com.example.DoAnJ2EE.dto.auth.LoginRequest;
import com.example.DoAnJ2EE.dto.auth.RefreshTokenRequest;
import com.example.DoAnJ2EE.dto.auth.RegisterRequest;
import com.example.DoAnJ2EE.dto.auth.RegisterResponse;
import com.example.DoAnJ2EE.entity.RefreshToken;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.UserRepository;
import com.example.DoAnJ2EE.security.JwtService;
import com.example.DoAnJ2EE.service.AuthService;
import com.example.DoAnJ2EE.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        String fullName = request.getFullName().trim();
        String email = request.getEmail().trim().toLowerCase();
        String phone = request.getPhone().trim();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        User user = User.builder()
                .username(email)
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .message("Đăng ký thành công, vui lòng đăng nhập")
                .email(user.getEmail())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Sai tài khoản hoặc mật khẩu"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Sai tài khoản hoặc mật khẩu");
        }

        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .username(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token không tồn tại"));

        if (!refreshTokenService.isValid(refreshToken)) {
            throw new RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .username(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }
}