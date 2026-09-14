package com.blinkit.application.admin.dtos.response;

import java.time.LocalDateTime;

public record AdminOrderResponse(
        Long orderId,
        String customerName,
        String customerEmail,
        String status,
        Double totalAmount,
        String deliveryAddress,
        LocalDateTime placedAt
) {}