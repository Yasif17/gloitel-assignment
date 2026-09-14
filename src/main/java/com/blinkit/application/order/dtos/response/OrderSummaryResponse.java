package com.blinkit.application.order.dtos.response;

import java.time.LocalDateTime;

public record OrderSummaryResponse(Long orderId, String status, Double totalAmount, Integer itemCount, LocalDateTime placedAt) {}
