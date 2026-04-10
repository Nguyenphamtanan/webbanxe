package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.CreateAppointmentRequest;
import com.example.DoAnJ2EE.dto.response.AppointmentResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateAppointmentRequest request
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Bạn chưa đăng nhập hoặc token không hợp lệ"
            ));
        }

        return ResponseEntity.ok(
                appointmentService.create(userDetails.getUser(), request)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<?> my(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Bạn chưa đăng nhập hoặc token không hợp lệ"
            ));
        }

        return ResponseEntity.ok(
                appointmentService.getMy(userDetails.getUser())
        );
    }
}