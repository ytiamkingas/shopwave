package com.shopwave.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request payload to add an item to the cart")
public class AddItemRequest {

    @Schema(description = "MongoDB product ID from Product Service", example = "664f1b2c3a4e5f6d7e8f9a0b", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product ID is required")
    private String productId;

    @Schema(description = "Display name of the product", example = "Apple iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name is required")
    private String productName;

    @Schema(description = "Product image URL for cart display", example = "https://cdn.shopwave.com/img/iphone15.jpg")
    private String productImageUrl;

    @Schema(description = "Current unit price of the product (INR)", example = "134900.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @DecimalMin("0.01")
    private BigDecimal unitPrice;

    @Schema(description = "Number of units to add", example = "1", minimum = "1", defaultValue = "1")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity = 1;
}
