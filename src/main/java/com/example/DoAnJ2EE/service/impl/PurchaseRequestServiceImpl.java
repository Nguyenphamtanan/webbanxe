package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.PurchaseRequestStatus;
import com.example.DoAnJ2EE.common.constant.RequestType;
import com.example.DoAnJ2EE.dto.request.CreatePurchaseRequestRequest;
import com.example.DoAnJ2EE.dto.request.QuoteRequestUpdateRequest;
import com.example.DoAnJ2EE.dto.request.UpdatePurchaseRequestStatusRequest;
import com.example.DoAnJ2EE.dto.response.AdminPurchaseRequestResponse;
import com.example.DoAnJ2EE.dto.response.MyPurchaseRequestResponse;
import com.example.DoAnJ2EE.dto.response.PurchaseRequestItemResponse;
import com.example.DoAnJ2EE.dto.response.PurchaseRequestResponse;
import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.CartItem;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.PurchaseRequest;
import com.example.DoAnJ2EE.entity.PurchaseRequestItem;
import com.example.DoAnJ2EE.entity.Region;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.CartItemRepository;
import com.example.DoAnJ2EE.repository.CartRepository;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.repository.PurchaseRequestItemRepository;
import com.example.DoAnJ2EE.repository.PurchaseRequestRepository;
import com.example.DoAnJ2EE.repository.RegionRepository;
import com.example.DoAnJ2EE.service.PurchaseRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.DoAnJ2EE.service.MailService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestItemRepository purchaseRequestItemRepository;
    private final RegionRepository regionRepository;
    private final MotorbikeRepository motorbikeRepository;
    private final MailService mailService;

    @Override
    @Transactional
    public PurchaseRequestResponse createFromCart(User user, CreatePurchaseRequestRequest request) {
        validateCreateRequest(request);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống");
        }

        Region region = null;
        if (request.getRegionId() != null) {
            region = regionRepository.findById(request.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Khu vực không tồn tại"));
        }

        RequestType requestType;
        try {
            requestType = RequestType.valueOf(request.getRequestType().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Loại yêu cầu không hợp lệ");
        }

        PurchaseRequest purchaseRequest = PurchaseRequest.builder()
                .requestCode(generateRequestCode())
                .user(user)
                .region(region)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .note(request.getNote())
                .requestType(requestType)
                .status(PurchaseRequestStatus.NEW)
                .build();

        purchaseRequest = purchaseRequestRepository.save(purchaseRequest);

        for (CartItem cartItem : cartItems) {
            BigDecimal unitPrice = cartItem.getMotorbike().getPrice();

            PurchaseRequestItem item = PurchaseRequestItem.builder()
                    .purchaseRequest(purchaseRequest)
                    .motorbike(cartItem.getMotorbike())
                    .motorbikeName(cartItem.getMotorbike().getName())
                    .unitPrice(unitPrice)
                    .quantity(cartItem.getQuantity())
                    .build();

            purchaseRequestItemRepository.save(item);
        }

        cartItemRepository.deleteByCart(cart);

        return mapToDetailResponse(purchaseRequest);
    }

    @Override
    @Transactional
    public PurchaseRequestResponse createQuoteRequest(User user, CreatePurchaseRequestRequest request) {
        validateQuoteRequest(request);

        Motorbike motorbike = motorbikeRepository.findById(request.getMotorbikeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        if (!Boolean.TRUE.equals(motorbike.getIsActive())) {
            throw new RuntimeException("Xe hiện không khả dụng");
        }

        Region region = null;
        if (request.getRegionId() != null) {
            region = regionRepository.findById(request.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Khu vực không tồn tại"));
        }

        PurchaseRequest purchaseRequest = PurchaseRequest.builder()
                .requestCode(generateRequestCode())
                .user(user)
                .region(region)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .note(request.getNote())
                .requestType(RequestType.QUOTE)
                .status(PurchaseRequestStatus.NEW)
                .build();

        purchaseRequest = purchaseRequestRepository.save(purchaseRequest);

        PurchaseRequestItem item = PurchaseRequestItem.builder()
                .purchaseRequest(purchaseRequest)
                .motorbike(motorbike)
                .motorbikeName(motorbike.getName())
                .unitPrice(motorbike.getPrice())
                .quantity(1)
                .build();

        purchaseRequestItemRepository.save(item);

        return mapToDetailResponse(purchaseRequest);
    }

    @Override
    public List<MyPurchaseRequestResponse> getMyRequests(User user) {
        return purchaseRequestRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(pr -> {

                    // 🔥 lấy item đầu tiên
                    PurchaseRequestItem item = purchaseRequestItemRepository
                            .findByPurchaseRequest(pr)
                            .stream()
                            .findFirst()
                            .orElse(null);

                    String name = null;
                    String image = null;
                    String slug = null;

                    if (item.getMotorbike() != null) {
                        name = item.getMotorbikeName();
                        image = item.getMotorbike().getPrimaryImageUrl();
                        slug = item.getMotorbike().getSlug();
                    }

                    return new MyPurchaseRequestResponse(
                            pr.getId(),
                            pr.getRequestCode(),
                            pr.getRequestType().name(),
                            pr.getStatus().name(),
                            pr.getQuotedPrice(),
                            pr.getDepositRequired(),
                            pr.getSalesNote(),
                            pr.getCreatedAt(),

                            // 🔥 new fields
                            name,
                            image,
                            slug
                    );
                })
                .toList();
    }

    @Override
    public List<AdminPurchaseRequestResponse> getAllForAdmin() {
        return purchaseRequestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(pr -> {

                    PurchaseRequestItem item = purchaseRequestItemRepository
                            .findByPurchaseRequest(pr)
                            .stream()
                            .findFirst()
                            .orElse(null);

                    String name = null;
                    String image = null;
                    String slug = null;

                    if (item != null) {
                        name = item.getMotorbikeName();

                        if (item.getMotorbike() != null) {
                            image = item.getMotorbike().getPrimaryImageUrl();
                            slug = item.getMotorbike().getSlug();
                        }
                    }

                    return new AdminPurchaseRequestResponse(
                            pr.getId(),
                            pr.getRequestCode(),
                            pr.getFullName(),
                            pr.getPhone(),
                            pr.getEmail(),
                            pr.getRequestType() != null ? pr.getRequestType().name() : null,
                            pr.getStatus() != null ? pr.getStatus().name() : null,
                            pr.getQuotedPrice(),
                            pr.getDepositRequired(),
                            pr.getSalesNote(),
                            pr.getCreatedAt(),
                            name,
                            image,
                            slug
                    );
                })
                .toList();
    }
    @Override
    public PurchaseRequestResponse getById(Long id) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        return mapToDetailResponse(purchaseRequest);
    }

    @Override
    @Transactional
    public PurchaseRequestResponse updateStatus(Long id, UpdatePurchaseRequestStatusRequest request) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        try {
            purchaseRequest.setStatus(PurchaseRequestStatus.valueOf(request.getStatus().toUpperCase()));
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái yêu cầu không hợp lệ");
        }

        purchaseRequest.setSalesNote(request.getSalesNote());

        purchaseRequestRepository.save(purchaseRequest);

        // Gửi mail phản hồi cho khách
        if (purchaseRequest.getEmail() != null && !purchaseRequest.getEmail().isBlank()) {
            String html = """
                <h3>Yêu cầu của bạn đã được showroom cập nhật</h3>
                <p><b>Mã yêu cầu:</b> %s</p>
                <p><b>Trạng thái mới:</b> %s</p>
                <p><b>Ghi chú phản hồi:</b> %s</p>
                """
                    .formatted(
                            purchaseRequest.getRequestCode(),
                            purchaseRequest.getStatus().name(),
                            purchaseRequest.getSalesNote() != null ? purchaseRequest.getSalesNote() : "Không có"
                    );

            mailService.sendMail(
                    purchaseRequest.getEmail(),
                    "Cập nhật yêu cầu từ showroom",
                    html
            );
        }

        return mapToDetailResponse(purchaseRequest);
    }

    @Override
    @Transactional
    public PurchaseRequestResponse updateQuote(Long id, QuoteRequestUpdateRequest request) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (purchaseRequest.getRequestType() != RequestType.QUOTE) {
            throw new RuntimeException("Chỉ áp dụng báo giá cho yêu cầu QUOTE");
        }


        purchaseRequest.setQuotedPrice(request.getQuotedPrice());
        purchaseRequest.setDepositRequired(request.getDepositRequired());
        purchaseRequest.setSalesNote(request.getSalesNote());
        purchaseRequest.setStatus(PurchaseRequestStatus.QUOTED);

        purchaseRequestRepository.save(purchaseRequest);

        if (purchaseRequest.getEmail() != null && !purchaseRequest.getEmail().isBlank()) {
            String html = """
                <h3>Showroom đã phản hồi yêu cầu báo giá của bạn</h3>
                <p>Mã yêu cầu: %s</p>
                <p>Giá báo: %s</p>
                <p>Tiền cọc: %s</p>
                <p>Ghi chú: %s</p>
                """
                    .formatted(
                            purchaseRequest.getRequestCode(),
                            purchaseRequest.getQuotedPrice(),
                            purchaseRequest.getDepositRequired(),
                            purchaseRequest.getSalesNote() != null ? purchaseRequest.getSalesNote() : ""
                    );

            mailService.sendMail(
                    purchaseRequest.getEmail(),
                    "Phản hồi báo giá từ showroom",
                    html
            );
        }

        return mapToDetailResponse(purchaseRequest);
    }

    private void validateCreateRequest(CreatePurchaseRequestRequest request) {
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống");
        }
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }
        if (request.getRequestType() == null || request.getRequestType().trim().isEmpty()) {
            throw new RuntimeException("Loại yêu cầu không được để trống");
        }
    }

    private void validateQuoteRequest(CreatePurchaseRequestRequest request) {
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống");
        }
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }
        if (request.getMotorbikeId() == null) {
            throw new RuntimeException("Thiếu thông tin xe");
        }
    }

    private String generateRequestCode() {
        return "REQ" + System.currentTimeMillis();
    }

    private PurchaseRequestResponse mapToDetailResponse(PurchaseRequest purchaseRequest) {

        List<PurchaseRequestItemResponse> items = purchaseRequestItemRepository
                .findByPurchaseRequest(purchaseRequest)
                .stream()
                .map(item -> {

                    Long motorbikeId = null;
                    String slug = null;
                    String image = null;

                    if (item.getMotorbike() != null) {
                        motorbikeId = item.getMotorbike().getId();
                        slug = item.getMotorbike().getSlug();
                        image = item.getMotorbike().getPrimaryImageUrl();
                    }

                    return new PurchaseRequestItemResponse(
                            item.getId(),
                            motorbikeId,
                            item.getMotorbikeName(),
                            slug,
                            image,
                            item.getUnitPrice(),
                            item.getQuantity(),
                            item.getLineTotal()
                    );
                })
                .toList();

        return new PurchaseRequestResponse(
                purchaseRequest.getId(),
                purchaseRequest.getRequestCode(),
                purchaseRequest.getRegion() != null ? purchaseRequest.getRegion().getId() : null,
                purchaseRequest.getFullName(),
                purchaseRequest.getPhone(),
                purchaseRequest.getEmail(),
                purchaseRequest.getAddress(),
                purchaseRequest.getNote(),
                purchaseRequest.getRequestType().name(),
                purchaseRequest.getStatus().name(),
                purchaseRequest.getQuotedPrice(),
                purchaseRequest.getDepositRequired(),
                purchaseRequest.getSalesNote(),
                purchaseRequest.getCreatedAt(),
                purchaseRequest.getUpdatedAt(),
                items
        );
    }
    @Override
    @Transactional
    public PurchaseRequestResponse customerDecision(Long id, User user, UpdatePurchaseRequestStatusRequest request) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (purchaseRequest.getUser() == null || !purchaseRequest.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xử lý yêu cầu này");
        }

        if (purchaseRequest.getStatus() != PurchaseRequestStatus.QUOTED) {
            throw new RuntimeException("Chỉ có thể phản hồi khi showroom đã báo giá");
        }

        PurchaseRequestStatus newStatus;
        try {
            newStatus = PurchaseRequestStatus.valueOf(request.getStatus().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }

        if (newStatus != PurchaseRequestStatus.APPROVED && newStatus != PurchaseRequestStatus.REJECTED) {
            throw new RuntimeException("Khách chỉ được chọn APPROVED hoặc REJECTED");
        }

        purchaseRequest.setStatus(newStatus);

        purchaseRequestRepository.save(purchaseRequest);
        return mapToDetailResponse(purchaseRequest);
    }
}