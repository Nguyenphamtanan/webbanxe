package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/profile")
@RequiredArgsConstructor
public class CustomerProfileApiController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Chưa đăng nhập"));
        }

        User user = userService.findByEmail(userDetails.getUser().getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Không tìm thấy thông tin người dùng"));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("username", user.getUsername());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("role", user.getRole() != null ? user.getRole().name() : "");

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody Map<String, String> payload) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Chưa đăng nhập"));
        }

        User user = userService.findByEmail(userDetails.getUser().getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Không tìm thấy thông tin người dùng"));
        }

        String fullName = payload.get("fullName");
        String phone = payload.get("phone");

        if (fullName == null || fullName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Họ tên không được để trống"));
        }

        user.setFullName(fullName.trim());
        user.setPhone(phone != null ? phone.trim() : "");

        userService.save(user);

        return ResponseEntity.ok(Map.of("message", "Cập nhật thông tin thành công"));
    }
}