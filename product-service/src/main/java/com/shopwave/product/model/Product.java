package com.shopwave.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Product document stored in MongoDB")
public class Product {

    @Id
    @Schema(description = "MongoDB auto-generated product ID", example = "664f1b2c3a4e5f6d7e8f9a0b", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotBlank(message = "Product name is required")
    @Indexed
    @Schema(description = "Name of the product", example = "Apple iPhone 15 Pro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Detailed product description", example = "Latest iPhone with A17 Pro chip and titanium design")
    private String description;

    @NotBlank(message = "Category is required")
    @Indexed
    @Schema(description = "Product category", example = "Electronics", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Selling price in INR", example = "134900.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Min(0)
    @Schema(description = "Available stock quantity", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Brand name", example = "Apple")
    private String brand;

    @Schema(description = "Stock Keeping Unit identifier", example = "APL-IP15P-256-BLK")
    private String sku;

    @Builder.Default
    @Schema(description = "List of image URLs for the product")
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    @Schema(description = "Flexible key-value product attributes, e.g. color, size, storage",
            example = "{\"color\": \"Black Titanium\", \"storage\": \"256GB\", \"warranty\": \"1 year\"}")
    private Map<String, Object> attributes = new HashMap<>();

    @Builder.Default
    @Schema(description = "Average customer rating (0.0 to 5.0)", example = "4.7", accessMode = Schema.AccessMode.READ_ONLY)
    private double averageRating = 0.0;

    @Builder.Default
    @Schema(description = "Total number of reviews", example = "128", accessMode = Schema.AccessMode.READ_ONLY)
    private int reviewCount = 0;

    @Builder.Default
    @Schema(description = "Whether the product is visible in catalog (false = soft deleted)", example = "true")
    private boolean active = true;

    @CreatedDate
    @Schema(description = "Timestamp when product was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp of last update", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
