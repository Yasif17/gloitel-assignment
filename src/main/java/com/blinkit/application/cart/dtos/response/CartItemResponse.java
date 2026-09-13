package com.blinkit.application.cart.dtos.response;

public record CartItemResponse(
        Long productId, String name, String image, Integer quantity,
        Double sellingPrice, Double lineTotal
) {}