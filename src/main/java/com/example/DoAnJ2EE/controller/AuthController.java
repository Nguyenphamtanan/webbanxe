package com.example.DoAnJ2EE.controller;

import com.example.DoAnJ2EE.dto.LoginRequest;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return authService.login(
                request.getUsername(),
                request.getPassword()
        );
    }
}