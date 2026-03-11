package com.shopwave.user.dto;

import com.shopwave.user.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication response containing JWT token and user info")
public class AuthResponse {

    @Schema(description = "JWT access token to use in Authorization header", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Token type, always 'Bearer'", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Unique ID of the authenticated user", example = "1")
    private Long userId;

    @Schema(description = "Email of the authenticated user", example = "john@example.com")
    private String email;

    @Schema(description = "Full name of the authenticated user", example = "John Doe")
    private String fullName;

    @Schema(description = "Role of the authenticated user", example = "CUSTOMER")
    private Role role;

    @Schema(description = "Token expiration time in seconds", example = "86400")
    private long expiresIn;
}
