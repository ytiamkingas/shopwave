package com.shopwave.order.dto;

import com.shopwave.order.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Admin request to update the status of an order")
public class UpdateStatusRequest {

    @Schema(description = "New order status", example = "SHIPPED",
            allowableValues = {"PENDING","CONFIRMED","PROCESSING","SHIPPED","DELIVERED","CANCELLED"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Status is required")
    private OrderStatus status;

    @Schema(description = "Courier tracking number (required when status = SHIPPED)", example = "DTDC123456789IN")
    private String trackingNumber;
}
