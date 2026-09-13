package com.blinkit.application.cart.dtos.response;

import java.util.List;

public record CartResponse(
        Long storeId, List<CartItemResponse> items,
        Double subtotal, Double deliveryFee, Double total
) {}