package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.request.AddToCartRequest;
import com.example.DoAnJ2EE.dto.request.UpdateCartItemRequest;
import com.example.DoAnJ2EE.dto.response.CartItemResponse;
import com.example.DoAnJ2EE.dto.response.CartResponse;
import com.example.DoAnJ2EE.entity.Cart;
import com.example.DoAnJ2EE.entity.CartItem;
import com.example.DoAnJ2EE.entity.Motorbike;
import com.example.DoAnJ2EE.entity.User;
import com.example.DoAnJ2EE.repository.CartItemRepository;
import com.example.DoAnJ2EE.repository.CartRepository;
import com.example.DoAnJ2EE.repository.MotorbikeRepository;
import com.example.DoAnJ2EE.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MotorbikeRepository motorbikeRepository;

    @Override
    public Optional<Cart> findByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }

    @Override
    public CartResponse getMyCart(User user) {
        Cart cart = getOrCreateCart(user);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return mapToCartResponse(cart, cartItems);
    }

    @Override
    @Transactional
    public CartResponse addToCart(User user, AddToCartRequest request) {
        if (request.getMotorbikeId() == null) {
            throw new RuntimeException("Thiếu motorbikeId");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        Cart cart = getOrCreateCart(user);

        Motorbike motorbike = motorbikeRepository.findById(request.getMotorbikeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        CartItem cartItem = cartItemRepository.findByCartAndMotorbike(cart, motorbike)
                .orElse(null);

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .motorbike(motorbike)
                    .quantity(request.getQuantity())
                    .build();
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        cartItemRepository.save(cartItem);

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return mapToCartResponse(cart, cartItems);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(User user, UpdateCartItemRequest request) {
        if (request.getCartItemId() == null) {
            throw new RuntimeException("Thiếu cartItemId");
        }
        if (request.getQuantity() == null) {
            throw new RuntimeException("Thiếu quantity");
        }

        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bạn không có quyền sửa sản phẩm này");
        }

        if (request.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return mapToCartResponse(cart, cartItems);
    }

    @Override
    @Transactional
    public CartResponse removeCartItem(User user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa sản phẩm này");
        }

        cartItemRepository.delete(cartItem);

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        return mapToCartResponse(cart, cartItems);
    }

    private CartResponse mapToCartResponse(Cart cart, List<CartItem> cartItems) {
        List<CartItemResponse> items = cartItems.stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getMotorbike().getPrice();
                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new CartItemResponse(
                            item.getId(),
                            item.getMotorbike().getId(),
                            item.getMotorbike().getName(),
                            item.getMotorbike().getPrimaryImageUrl(),
                            unitPrice,
                            item.getQuantity(),
                            lineTotal
                    );
                })
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = items.stream()
                .map(CartItemResponse::getQuantity)
                .reduce(0, Integer::sum);

        return new CartResponse(
                cart.getId(),
                items,
                totalAmount,
                totalItems
        );
    }
}