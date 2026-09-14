package com.blinkit.application.order.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(
        @NotBlank(message = "Delivery address is required") String deliveryAddress
) {}