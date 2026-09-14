package com.blinkit.application.order.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId, String status, List<OrderItemResponse> items,
        Double subtotal, Double deliveryFee, Double totalAmount,
        String deliveryAddress, LocalDateTime placedAt
) {}