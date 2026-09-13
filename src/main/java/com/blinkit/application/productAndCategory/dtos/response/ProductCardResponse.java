package com.blinkit.application.productAndCategory.dtos.response;

import lombok.Builder;

@Builder
public record ProductCardResponse(
        Long id, String slug, String name, String image,
        Double mrp, Double sellingPrice, String unit, boolean inStock
) {}