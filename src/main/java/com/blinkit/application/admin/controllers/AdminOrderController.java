package com.blinkit.application.admin.controllers;


import com.blinkit.application.admin.dtos.requests.OrderStatusUpdateRequest;
import com.blinkit.application.admin.dtos.response.AdminOrderResponse;
import com.blinkit.application.order.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        orderService.updateOrderStatus(id, request.status());
        return ResponseEntity.noContent().build();
    }
}