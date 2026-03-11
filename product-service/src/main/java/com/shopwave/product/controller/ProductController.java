package com.shopwave.product.controller;

import com.shopwave.product.model.Product;
import com.shopwave.product.service.ProductService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product catalog — GET endpoints are public, write endpoints require ADMIN")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "List products with optional filters",
               description = "Returns all active products. Supports filtering by category, name search, and price range. No authentication required.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product list returned",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class))))
    })
    public ResponseEntity<List<Product>> getProducts(
            @Parameter(description = "Filter by category name", example = "Electronics") @RequestParam(required = false) String category,
            @Parameter(description = "Search by product name (case-insensitive)", example = "iPhone") @RequestParam(required = false) String search,
            @Parameter(description = "Minimum price filter", example = "1000.00") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price filter", example = "50000.00") @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getProducts(category, search, minPrice, maxPrice));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getById(
            @Parameter(description = "MongoDB product ID", example = "664f1b2c3a4e5f6d7e8f9a0b") @PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "[ADMIN] Create a new product")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    })
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "[ADMIN] Update an existing product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    })
    public ResponseEntity<Product> update(@PathVariable String id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "[ADMIN] Soft-delete a product",
               description = "Sets active=false. Product disappears from catalog but data is retained.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product soft-deleted"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    })
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "[ADMIN] Reduce stock quantity by given amount")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock updated"),
        @ApiResponse(responseCode = "409", description = "Insufficient stock"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> updateStock(
            @PathVariable String id,
            @Parameter(description = "Quantity to deduct from stock", example = "5") @RequestParam int quantity) {
        productService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "[ADMIN] Get products at or below stock threshold",
               description = "Useful for inventory management. Default threshold is 10 units.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Low-stock products returned")
    })
    public ResponseEntity<List<Product>> getLowStock(
            @Parameter(description = "Stock threshold (inclusive)", example = "10") @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(productService.getLowStock(threshold));
    }
}
