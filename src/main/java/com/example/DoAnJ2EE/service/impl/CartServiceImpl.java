package com.example.DoAnJ2EE.service.impl;

import com.example.DoAnJ2EE.dto.cart.CartItemResponse;
import com.example.DoAnJ2EE.dto.cart.CartResponse;
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
        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addToCart(User user, Long motorbikeId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        Cart cart = getOrCreateCart(user);

        Motorbike motorbike = motorbikeRepository.findById(motorbikeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        CartItem cartItem = cartItemRepository.findByCartAndMotorbike(cart, motorbike)
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .motorbike(motorbike)
                        .quantity(0)
                        .build());

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);

        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(User user, Long cartItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(User user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        List<CartItemResponse> itemResponses = cartItems.stream().map(item -> {
            BigDecimal unitPrice = item.getMotorbike().getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            return CartItemResponse.builder()
                    .cartItemId(item.getId())
                    .motorbikeId(item.getMotorbike().getId())
                    .motorbikeName(item.getMotorbike().getName())
                    .unitPrice(unitPrice)
                    .quantity(item.getQuantity())
                    .lineTotal(lineTotal)
                    .build();
        }).toList();

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .build();
    }
}