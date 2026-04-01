package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.response.DepositResponse;
import com.example.DoAnJ2EE.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/deposits")
@RequiredArgsConstructor
public class AdminDepositApiController {

    private final DepositService depositService;

    @GetMapping
    public ResponseEntity<List<DepositResponse>> getAll() {
        return ResponseEntity.ok(depositService.getAllForAdmin());
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<DepositResponse> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(depositService.adminConfirm(id));
    }
}