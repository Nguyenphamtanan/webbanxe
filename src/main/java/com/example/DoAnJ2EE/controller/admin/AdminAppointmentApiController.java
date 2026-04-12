package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.request.UpdateAppointmentStatusRequest;
import com.example.DoAnJ2EE.dto.response.AdminAppointmentResponse;
import com.example.DoAnJ2EE.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/appointments")
@RequiredArgsConstructor
public class AdminAppointmentApiController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AdminAppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllForAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminAppointmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getByIdForAdmin(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdminAppointmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateAppointmentStatusRequest request
    ) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request));
    }
}