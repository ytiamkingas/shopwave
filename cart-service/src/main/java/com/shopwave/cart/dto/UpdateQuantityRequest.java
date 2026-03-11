package com.shopwave.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Request payload to update the quantity of a cart item")
public class UpdateQuantityRequest {

    @Schema(description = "New quantity for the item (replaces current quantity)", example = "3", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
