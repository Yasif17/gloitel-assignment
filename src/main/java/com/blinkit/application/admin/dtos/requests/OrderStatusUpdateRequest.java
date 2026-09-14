package com.blinkit.application.admin.dtos.requests;

import com.blinkit.application.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(
        @NotNull(message = "Status is required")
        OrderStatus status
) {}