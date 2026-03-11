package com.example.DoAnJ2EE.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/user")
    @PreAuthorize("hasRole('USER')")
    public String user(){
        return "Hello User";
    }
}