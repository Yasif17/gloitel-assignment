package com.blinkit.application.cart.services;

import com.blinkit.application.cart.dtos.requests.CartItemRequest;
import com.blinkit.application.cart.dtos.response.CartItemResponse;
import com.blinkit.application.cart.dtos.response.CartResponse;

public interface CartService {

    public CartResponse addProductToCart(Long userId, Long storeId, CartItemRequest cartItemRequest);

    public CartResponse updateQuantity(Long userId, Long productId, int quantity);

    public CartResponse removeProductFromCart(Long userId, Long productId);

    public CartResponse getCart(Long userId);

    public void clearCart(Long userId);

}
