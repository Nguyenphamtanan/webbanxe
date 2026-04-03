package com.example.DoAnJ2EE.security;

import com.example.DoAnJ2EE.common.constant.UserRole;
import com.example.DoAnJ2EE.entity.RefreshToken;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.UserRepository;
import com.example.DoAnJ2EE.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            response.sendRedirect("/login?error=google_email_not_found");
            return;
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .username(email)
                    .fullName(fullName != null && !fullName.isBlank() ? fullName : email)
                    .email(email)
                    .phone("temp_" + System.currentTimeMillis())
                    .passwordHash(passwordEncoder.encode("GOOGLE_LOGIN_NO_PASSWORD"))
                    .role(UserRole.CUSTOMER)
                    .isActive(true)
                    .build();

            return userRepository.save(newUser);
        });

        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        String redirectUrl = "/oauth2-success"
                + "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken.getToken(), StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(user.getRole().name(), StandardCharsets.UTF_8)
                + "&username=" + URLEncoder.encode(user.getFullName(), StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}