package com.example.DoAnJ2EE.controller.admin;

import com.example.DoAnJ2EE.dto.request.QuoteRequestUpdateRequest;
import com.example.DoAnJ2EE.dto.request.UpdatePurchaseRequestStatusRequest;
import com.example.DoAnJ2EE.dto.response.AdminPurchaseRequestResponse;
import com.example.DoAnJ2EE.dto.response.PurchaseRequestResponse;
import com.example.DoAnJ2EE.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/requests")
@RequiredArgsConstructor
public class AdminPurchaseRequestApiController {

    private final PurchaseRequestService purchaseRequestService;

    @GetMapping
    public ResponseEntity<List<AdminPurchaseRequestResponse>> getAllRequests() {
        return ResponseEntity.ok(purchaseRequestService.getAllForAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequestResponse> getRequestDetail(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(purchaseRequestService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PurchaseRequestResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdatePurchaseRequestStatusRequest request
    ) {
        return ResponseEntity.ok(purchaseRequestService.updateStatus(id, request));
    }

    @PutMapping("/{id}/quote")
    public ResponseEntity<PurchaseRequestResponse> updateQuote(
            @PathVariable Long id,
            @RequestBody QuoteRequestUpdateRequest request
    ) {
        return ResponseEntity.ok(purchaseRequestService.updateQuote(id, request));
    }
}