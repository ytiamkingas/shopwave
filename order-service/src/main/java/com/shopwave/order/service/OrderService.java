package com.shopwave.order.service;

import com.shopwave.order.dto.*;
import com.shopwave.order.model.*;
import com.shopwave.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order placeOrder(Long userId, PlaceOrderRequest req) {
        Order order = Order.builder()
            .userId(userId)
            .shippingAddress(req.getShippingAddress())
            .paymentMethod(req.getPaymentMethod())
            .notes(req.getNotes())
            .status(OrderStatus.PENDING)
            .build();

        req.getItems().forEach(itemReq -> {
            OrderItem item = OrderItem.builder()
                .productId(itemReq.getProductId())
                .productName(itemReq.getProductName())
                .productImageUrl(itemReq.getProductImageUrl())
                .quantity(itemReq.getQuantity())
                .unitPrice(itemReq.getUnitPrice())
                .subtotal(itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())))
                .build();
            order.addItem(item);
        });

        BigDecimal total = order.getItems().stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Order getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied for order: " + orderId);
        }
        return order;
    }

    @Transactional
    public Order updateStatus(Long orderId, UpdateStatusRequest req) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(req.getStatus());
        if (req.getTrackingNumber() != null) order.setTrackingNumber(req.getTrackingNumber());
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId, userId);
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel order with status: " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}
