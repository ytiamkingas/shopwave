package com.shopwave.order.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Order entity stored in PostgreSQL")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated order ID", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID of the user who placed this order", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Schema(description = "Line items in this order")
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "Current order status", example = "PENDING")
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Total order amount (sum of all item subtotals)", example = "269800.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    @Schema(description = "Delivery address", example = "42, MG Road, Bhubaneswar, Odisha - 751001")
    private String shippingAddress;

    @Schema(description = "Payment method used", example = "CASH_ON_DELIVERY")
    private String paymentMethod;

    @Schema(description = "Courier tracking number (set when SHIPPED)", example = "DTDC123456789IN")
    private String trackingNumber;

    @Schema(description = "Customer delivery notes", example = "Please deliver after 6 PM")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "Timestamp when order was placed", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Timestamp of last status update", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
