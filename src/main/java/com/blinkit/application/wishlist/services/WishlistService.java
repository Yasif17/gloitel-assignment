package com.blinkit.application.wishlist.services;

import com.blinkit.application.wishlist.dtos.response.WishlistResponse;

public interface WishlistService {

    WishlistResponse addItem(Long userId, Long productId);
    void removeItem(Long userId, Long productId);
    WishlistResponse getWishlist(Long userId);
}
