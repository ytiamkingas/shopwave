package com.shopwave.cart.controller;

import com.shopwave.cart.dto.*;
import com.shopwave.cart.model.Cart;
import com.shopwave.cart.service.CartService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Cart", description = "Shopping cart — add, update, remove items, and checkout preparation")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get current user's cart",
               description = "Returns the full cart with all items, subtotals, and total price. Creates an empty cart if none exists.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart returned",
            content = @Content(schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<Cart> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(getUserId(auth)));
    }

    @PostMapping("/items")
    @Operation(summary = "Add an item to cart",
               description = "Adds a product to cart. If product already exists in cart, the quantity is incremented.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item added, updated cart returned",
            content = @Content(schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<Cart> addItem(Authentication auth, @Valid @RequestBody AddItemRequest req) {
        return ResponseEntity.ok(cartService.addItem(getUserId(auth), req));
    }

    @PutMapping("/items/{productId}")
    @Operation(summary = "Update quantity of a cart item",
               description = "Sets the quantity to the specified value (does not add to existing). To remove, use DELETE instead.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quantity updated, cart returned"),
        @ApiResponse(responseCode = "404", description = "Cart or item not found"),
        @ApiResponse(responseCode = "400", description = "Quantity must be at least 1")
    })
    public ResponseEntity<Cart> updateQuantity(
            Authentication auth,
            @Parameter(description = "Product ID of the item to update", example = "664f1b2c3a4e5f6d7e8f9a0b") @PathVariable String productId,
            @Valid @RequestBody UpdateQuantityRequest req) {
        return ResponseEntity.ok(cartService.updateItemQuantity(getUserId(auth), productId, req));
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove a specific item from cart")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item removed, updated cart returned"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<Cart> removeItem(
            Authentication auth,
            @Parameter(description = "Product ID of the item to remove", example = "664f1b2c3a4e5f6d7e8f9a0b") @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeItem(getUserId(auth), productId));
    }

    @DeleteMapping
    @Operation(summary = "Clear the entire cart",
               description = "Removes all items from the cart. Typically called after a successful order is placed.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cart cleared"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<Void> clearCart(Authentication auth) {
        cartService.clearCart(getUserId(auth));
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getCredentials();
    }
}
