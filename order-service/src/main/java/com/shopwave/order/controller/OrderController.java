package com.shopwave.order.controller;

import com.shopwave.order.dto.*;
import com.shopwave.order.model.*;
import com.shopwave.order.service.OrderService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Orders", description = "Order lifecycle — place, track, cancel, and manage orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place a new order",
               description = "Creates a new order with PENDING status. Include all item details including price snapshots.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order placed successfully",
            content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Validation error — check items list and address"),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<Order> placeOrder(
            Authentication auth,
            @Valid @RequestBody PlaceOrderRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.placeOrder(getUserId(auth), req));
    }

    @GetMapping
    @Operation(summary = "Get current user's order history",
               description = "Returns all orders for the authenticated user, sorted by most recent first.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order history returned",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Order.class)))),
        @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<List<Order>> getMyOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getUserOrders(getUserId(auth)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific order by ID",
               description = "Users can only access their own orders. Returns 404 for other users' orders (security by obscurity).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order details returned"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "403", description = "Order belongs to a different user")
    })
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "Order ID", example = "101") @PathVariable Long id,
            Authentication auth) {
        return ResponseEntity.ok(orderService.getOrderById(id, getUserId(auth)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Update order status",
               description = "Advance order through: PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED. Include trackingNumber when setting SHIPPED.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order status updated"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    })
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest req) {
        return ResponseEntity.ok(orderService.updateStatus(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an order",
               description = "Can only cancel PENDING or CONFIRMED orders. PROCESSING/SHIPPED orders cannot be cancelled.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Order cancelled"),
        @ApiResponse(responseCode = "409", description = "Order cannot be cancelled in its current status"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> cancelOrder(
            @Parameter(description = "Order ID to cancel", example = "101") @PathVariable Long id,
            Authentication auth) {
        orderService.cancelOrder(id, getUserId(auth));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] List all orders by status",
               description = "Useful for admin dashboard. Valid statuses: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Filtered order list returned"),
        @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    })
    public ResponseEntity<List<Order>> getByStatus(
            @Parameter(description = "Order status filter", example = "PENDING") @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getCredentials();
    }
}
