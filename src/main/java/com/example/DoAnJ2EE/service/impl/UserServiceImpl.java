package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.UserRepository;
import com.example.DoAnJ2EE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.DoAnJ2EE.common.constant.UserRole;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // ===== AUTH =====
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    // ===== ADMIN =====
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
    }

    @Override
    public User updateStatus(Long id, Boolean isActive) {
        User user = findById(id);
        user.setIsActive(isActive);
        return userRepository.save(user);
    }

    @Override
    public User updateRole(Long id, String role) {
        User user = findById(id);

        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            user.setRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Role không hợp lệ");
        }

        return userRepository.save(user);
    }
}