package com.blinkit.application.wishlist.dtos.response;

public record WishlistItemResponse(
        Long productId,
        String slug,
        String name,
        String image,
        Double mrp,
        Double sellingPrice,
        String unit
) {}