package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.common.constant.OrderStatus;
import com.example.DoAnJ2EE.common.constant.PaymentMethod;
import com.example.DoAnJ2EE.common.constant.PaymentStatus;
import com.example.DoAnJ2EE.dto.checkout.CheckoutRequest;
import com.example.DoAnJ2EE.dto.checkout.CheckoutResponse;
import com.example.DoAnJ2EE.dto.paypal.PayPalCaptureResponse;
import com.example.DoAnJ2EE.dto.paypal.PayPalCreateOrderResponse;
import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.CartItem;
import com.example.DoAnJ2EE.entity.Order;
import com.example.DoAnJ2EE.entity.OrderItem;
import com.example.DoAnJ2EE.entity.Region;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.CartItemRepository;
import com.example.DoAnJ2EE.repository.OrderItemRepository;
import com.example.DoAnJ2EE.repository.OrderRepository;
import com.example.DoAnJ2EE.repository.RegionRepository;
import com.example.DoAnJ2EE.service.CartService;
import com.example.DoAnJ2EE.service.OrderService;
import com.example.DoAnJ2EE.service.PayPalService;
import com.example.DoAnJ2EE.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final RegionRepository regionRepository;
    private final PayPalService payPalService;
    private final UserService userService;

    // ===== USER =====
    @Override
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getOrdersByCustomerEmail(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public CheckoutResponse checkoutCod(User user, CheckoutRequest request) {
        Cart cart = cartService.getOrCreateCart(user);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống");
        }

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực"));

        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getMotorbike().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .orderCode(generateOrderCode())
                .user(user)
                .region(region)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.COD)
                .paymentStatus(PaymentStatus.UNPAID)
                .build();

        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .motorbike(cartItem.getMotorbike())
                    .motorbikeName(cartItem.getMotorbike().getName())
                    .unitPrice(cartItem.getMotorbike().getPrice())
                    .quantity(cartItem.getQuantity())
                    .build();

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByCart(cart);

        return CheckoutResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .status(order.getStatus().name())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .totalAmount(order.getTotalAmount())
                .build();
    }

    // ===== ADMIN =====
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

    private String generateOrderCode() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    @Override
    @Transactional
    public PayPalCreateOrderResponse createPaypalOrder(User user, CheckoutRequest request) {
        Cart cart = cartService.getOrCreateCart(user);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống");
        }

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực"));

        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getMotorbike().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .orderCode(generateOrderCode())
                .user(user)
                .region(region)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.PAYPAL)
                .paymentStatus(PaymentStatus.UNPAID)
                .build();

        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .motorbike(cartItem.getMotorbike())
                    .motorbikeName(cartItem.getMotorbike().getName())
                    .unitPrice(cartItem.getMotorbike().getPrice())
                    .quantity(cartItem.getQuantity())
                    .build();

            orderItemRepository.save(orderItem);
        }

        String accessToken = payPalService.getAccessToken();
        String responseJson = payPalService.createOrder(accessToken, totalAmount.toPlainString(), "USD");

        org.json.JSONObject json = new org.json.JSONObject(responseJson);
        String paypalOrderId = json.getString("id");

        order.setPaypalOrderId(paypalOrderId);
        orderRepository.save(order);

        return PayPalCreateOrderResponse.builder()
                .localOrderId(order.getId())
                .localOrderCode(order.getOrderCode())
                .paypalOrderId(paypalOrderId)
                .approveUrl("")
                .build();
    }

    @Override
    @Transactional
    public PayPalCaptureResponse capturePaypalOrder(User user, Long orderId, String paypalOrderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền với đơn hàng này");
        }

        String accessToken = payPalService.getAccessToken();
        payPalService.captureOrder(accessToken, paypalOrderId);

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setPaypalOrderId(paypalOrderId);
        orderRepository.save(order);

        Cart cart = cartService.getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);

        return PayPalCaptureResponse.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .paypalOrderId(paypalOrderId)
                .paymentStatus(order.getPaymentStatus().name())
                .status(order.getStatus().name())
                .build();
    }
}