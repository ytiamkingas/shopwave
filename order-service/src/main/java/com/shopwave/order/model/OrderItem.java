package com.shopwave.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "A single line item within an order")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated order item ID", example = "55", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Order order;

    @Column(nullable = false)
    @Schema(description = "MongoDB product ID (reference to Product Service)", example = "664f1b2c3a4e5f6d7e8f9a0b")
    private String productId;

    @Column(nullable = false)
    @Schema(description = "Snapshot of product name at time of order", example = "Apple iPhone 15 Pro")
    private String productName;

    @Schema(description = "Snapshot of product image URL at time of order")
    private String productImageUrl;

    @Column(nullable = false)
    @Schema(description = "Quantity ordered", example = "2")
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Price per unit at time of order", example = "134900.00")
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Computed subtotal (unitPrice × quantity)", example = "269800.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal subtotal;

    @PrePersist @PreUpdate
    private void computeSubtotal() {
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
