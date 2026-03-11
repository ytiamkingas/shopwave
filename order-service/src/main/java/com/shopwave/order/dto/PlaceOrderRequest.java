package com.shopwave.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Request payload to place a new order")
public class PlaceOrderRequest {

    @Schema(description = "List of items to order — must contain at least one item", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    @Schema(description = "Full delivery address", example = "42, MG Road, Bhubaneswar, Odisha - 751001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @Schema(description = "Payment method", example = "CASH_ON_DELIVERY", allowableValues = {"CASH_ON_DELIVERY", "UPI", "CREDIT_CARD", "DEBIT_CARD", "NET_BANKING"}, defaultValue = "CASH_ON_DELIVERY")
    private String paymentMethod = "CASH_ON_DELIVERY";

    @Schema(description = "Optional delivery notes", example = "Please deliver after 6 PM")
    private String notes;
}
