package com.shopwave.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request payload for updating a user's profile")
public class UpdateProfileRequest {

    @Schema(description = "Updated full name", example = "John Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String fullName;

    @Schema(description = "Updated phone number", example = "+91-9876543210")
    private String phone;
}
