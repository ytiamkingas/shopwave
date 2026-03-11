package com.shopwave.cart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "A single item embedded in the Cart document")
public class CartItem {

    @Schema(description = "MongoDB product ID", example = "664f1b2c3a4e5f6d7e8f9a0b")
    private String productId;

    @Schema(description = "Product name at time of adding to cart", example = "Apple iPhone 15 Pro")
    private String productName;

    @Schema(description = "Product image URL", example = "https://cdn.shopwave.com/img/iphone15.jpg")
    private String productImageUrl;

    @Schema(description = "Current unit price (INR)", example = "134900.00")
    private BigDecimal unitPrice;

    @Schema(description = "Number of units in cart", example = "2")
    private int quantity;

    @Schema(description = "Subtotal for this item (unitPrice × quantity)", example = "269800.00", accessMode = Schema.AccessMode.READ_ONLY)
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
