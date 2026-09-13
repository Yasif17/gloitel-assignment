package com.blinkit.application.productAndCategory.dtos.response;

public record ProductDetailResponse(
        Long id, String slug, String name, String image, String description,
        Double mrp, Double sellingPrice, String unit,
        String categoryName, Integer availableQty
) {}