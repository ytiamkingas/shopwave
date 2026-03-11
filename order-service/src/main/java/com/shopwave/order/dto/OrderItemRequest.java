package com.shopwave.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "A single line item in an order request")
public class OrderItemRequest {

    @Schema(description = "MongoDB product ID from the Product Service", example = "664f1b2c3a4e5f6d7e8f9a0b", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product ID is required")
    private String productId;

    @Schema(description = "Product name snapshot (stored for historical record)", example = "Apple iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name is required")
    private String productName;

    @Schema(description = "Product image URL snapshot", example = "https://cdn.shopwave.com/img/iphone15.jpg")
    private String productImageUrl;

    @Schema(description = "Number of units ordered", example = "2", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @Min(1)
    private Integer quantity;

    @Schema(description = "Price per unit at time of order (INR)", example = "134900.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @DecimalMin("0.01")
    private BigDecimal unitPrice;
}
