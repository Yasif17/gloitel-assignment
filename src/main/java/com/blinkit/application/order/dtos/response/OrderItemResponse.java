package com.blinkit.application.order.dtos.response;

public record OrderItemResponse(Long productId, String name, String image, Integer quantity, Double priceAtOrder, Double lineTotal) {}
