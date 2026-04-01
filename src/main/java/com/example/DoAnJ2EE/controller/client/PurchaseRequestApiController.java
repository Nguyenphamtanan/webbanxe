package com.example.DoAnJ2EE.controller.client;

import com.example.DoAnJ2EE.dto.request.CreatePurchaseRequestRequest;
import com.example.DoAnJ2EE.dto.request.UpdatePurchaseRequestStatusRequest;
import com.example.DoAnJ2EE.dto.response.MyPurchaseRequestResponse;
import com.example.DoAnJ2EE.dto.response.PurchaseRequestResponse;
import com.example.DoAnJ2EE.security.CustomUserDetails;
import com.example.DoAnJ2EE.service.PurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class PurchaseRequestApiController {

    private final PurchaseRequestService purchaseRequestService;

    @PostMapping
    public ResponseEntity<PurchaseRequestResponse> createRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreatePurchaseRequestRequest request
    ) {
        return ResponseEntity.ok(
                purchaseRequestService.createFromCart(userDetails.getUser(), request)
        );
    }

    @PostMapping("/quote")
    public ResponseEntity<PurchaseRequestResponse> createQuoteRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreatePurchaseRequestRequest request
    ) {
        return ResponseEntity.ok(
                purchaseRequestService.createQuoteRequest(userDetails.getUser(), request)
        );
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<MyPurchaseRequestResponse>> getMyRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                purchaseRequestService.getMyRequests(userDetails.getUser())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequestResponse> getRequestDetail(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseRequestService.getById(id));
    }

    @PutMapping("/{id}/decision")
    public ResponseEntity<PurchaseRequestResponse> customerDecision(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePurchaseRequestStatusRequest request
    ) {
        return ResponseEntity.ok(
                purchaseRequestService.customerDecision(id, userDetails.getUser(), request)
        );
    }
}