package com.shopwave.cart.service;

import com.shopwave.cart.dto.*;
import com.shopwave.cart.model.*;
import com.shopwave.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> Cart.builder().userId(userId).build());
    }

    public Cart addItem(Long userId, AddItemRequest req) {
        Cart cart = getCart(userId);
        Optional<CartItem> existing = cart.getItems().stream()
            .filter(i -> i.getProductId().equals(req.getProductId()))
            .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + req.getQuantity());
        } else {
            cart.getItems().add(CartItem.builder()
                .productId(req.getProductId())
                .productName(req.getProductName())
                .productImageUrl(req.getProductImageUrl())
                .unitPrice(req.getUnitPrice())
                .quantity(req.getQuantity())
                .build());
        }
        return cartRepository.save(cart);
    }

    public Cart updateItemQuantity(Long userId, String productId, UpdateQuantityRequest req) {
        Cart cart = getOrThrow(userId);
        cart.getItems().stream()
            .filter(i -> i.getProductId().equals(productId))
            .findFirst()
            .ifPresent(i -> i.setQuantity(req.getQuantity()));
        return cartRepository.save(cart);
    }

    public Cart removeItem(Long userId, String productId) {
        Cart cart = getOrThrow(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = getOrThrow(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
    }
}
