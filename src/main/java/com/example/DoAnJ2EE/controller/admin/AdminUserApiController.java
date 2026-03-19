package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserApiController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PutMapping("/{id}/status")
    public User updateStatus(@PathVariable Long id,
                             @RequestParam Boolean isActive) {
        return userService.updateStatus(id, isActive);
    }

    @PutMapping("/{id}/role")
    public User updateRole(@PathVariable Long id,
                           @RequestParam String role) {
        return userService.updateRole(id, role);
    }
}