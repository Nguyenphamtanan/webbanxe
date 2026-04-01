package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.OrderStatus;
import com.example.DoAnJ2EE.common.constant.PaymentMethod;
import com.example.DoAnJ2EE.common.constant.PaymentStatus;
import com.example.DoAnJ2EE.dto.request.CheckoutRequest;
import com.example.DoAnJ2EE.dto.response.CheckoutResponse;
import com.example.DoAnJ2EE.dto.response.MyOrderResponse;
import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.CartItem;
import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.OrderItem;
import com.example.DoAnJ2EE.entity.Region;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.CartItemRepository;
import com.example.DoAnJ2EE.repository.CartRepository;
import com.example.DoAnJ2EE.repository.OrderItemRepository;
import com.example.DoAnJ2EE.repository.OrderRepository;
import com.example.DoAnJ2EE.repository.RegionRepository;
import com.example.DoAnJ2EE.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final RegionRepository regionRepository;

    @Override
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public CheckoutResponse checkoutCod(User user, CheckoutRequest request) {
        validateCheckoutRequest(request);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống");
        }

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Khu vực không tồn tại"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal unitPrice = item.getMotorbike().getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);
        }

        Order order = Order.builder()
                .orderCode(generateOrderCode())
                .user(user)
                .region(region)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .totalAmount(totalAmount)
                .status(OrderStatus.WAITING_CONFIRMATION)                .paymentMethod(PaymentMethod.COD)
                .paymentStatus(PaymentStatus.UNPAID)
                .build();

        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            BigDecimal unitPrice = item.getMotorbike().getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .motorbike(item.getMotorbike())
                    .motorbikeName(item.getMotorbike().getName())
                    .unitPrice(unitPrice)
                    .quantity(item.getQuantity())
                    .build();

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByCart(cart);

        return new CheckoutResponse(
                order.getId(),
                order.getOrderCode(),
                order.getPaymentMethod().name(),
                order.getPaymentStatus().name(),
                "Đặt hàng COD thành công"
        );
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
    }

    @Override
    public Order updateStatus(Long id, String status) {
        Order order = findById(id);

        try {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }

        return orderRepository.save(order);
    }

    private void validateCheckoutRequest(CheckoutRequest request) {
        if (request.getReceiverName() == null || request.getReceiverName().trim().isEmpty()) {
            throw new RuntimeException("Tên người nhận không được để trống");
        }
        if (request.getReceiverPhone() == null || request.getReceiverPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            throw new RuntimeException("Địa chỉ giao hàng không được để trống");
        }
        if (request.getRegionId() == null) {
            throw new RuntimeException("Vui lòng chọn khu vực");
        }
    }

    private String generateOrderCode() {
        return "ORD" + System.currentTimeMillis();
    }
    @Override
    public List<MyOrderResponse> getMyOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(order -> new MyOrderResponse(
                        order.getId(),
                        order.getOrderCode(),
                        order.getTotalAmount(),
                        order.getStatus().name(),
                        order.getPaymentMethod().name(),
                        order.getPaymentStatus().name(),
                        order.getReceiverName(),
                        order.getReceiverPhone(),
                        order.getShippingAddress(),
                        order.getCreatedAt()
                ))
                .toList();
    }
}