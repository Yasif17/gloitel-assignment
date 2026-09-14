package com.blinkit.application.order.controllers;

import com.blinkit.application.order.dtos.requests.PlaceOrderRequest;
import com.blinkit.application.order.dtos.response.OrderResponse;
import com.blinkit.application.order.dtos.response.OrderSummaryResponse;
import com.blinkit.application.order.services.OrderService;
import com.blinkit.application.userAuth.exceptions.UserNotFoundException;
import com.blinkit.application.userAuth.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    private Long currentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody PlaceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(currentUserId(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryResponse>> getHistory() {
        return ResponseEntity.ok(orderService.getOrderHistory(currentUserId()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(currentUserId(), orderId));
    }
}