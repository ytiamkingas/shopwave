package com.shopwave.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request payload for logging in")
public class LoginRequest {

    @Schema(description = "Registered email address", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Email
    private String email;

    @Schema(description = "Account password", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
}
