package com.shopwave.cart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "carts")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Shopping cart document stored in MongoDB. One cart per user.")
public class Cart {

    @Id
    @Schema(description = "MongoDB auto-generated cart ID", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Indexed(unique = true)
    @Schema(description = "Owner's user ID (unique index — one cart per user)", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Builder.Default
    @Schema(description = "List of items currently in the cart")
    private List<CartItem> items = new ArrayList<>();

    @CreatedDate
    @Schema(description = "Timestamp when cart was first created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp of last cart modification", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Computed total price of all items in cart (INR)", example = "269800.00", accessMode = Schema.AccessMode.READ_ONLY)
    public BigDecimal getTotalPrice() {
        return items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Schema(description = "Computed total number of units across all items", example = "3", accessMode = Schema.AccessMode.READ_ONLY)
    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
