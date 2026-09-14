package com.blinkit.application.wishlist.dtos.response;

import java.util.List;

public record WishlistResponse(
        List<WishlistItemResponse> items,
        int totalItems
) {}