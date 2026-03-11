package com.example.DoAnJ2EE.service;

import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.UserRepository;
import com.example.DoAnJ2EE.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserRepository repo, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @Override
    public User register(User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");

        return repo.save(user);
    }

    @Override
    public String login(String username, String password) {

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return jwtUtil.generateToken(username);
    }
}