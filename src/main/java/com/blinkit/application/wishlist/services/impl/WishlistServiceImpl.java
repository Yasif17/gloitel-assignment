package com.blinkit.application.wishlist.services.impl;

import com.blinkit.application.productAndCategory.entities.Product;
import com.blinkit.application.productAndCategory.exceptions.ProductNotFoundException;
import com.blinkit.application.productAndCategory.repositories.ProductRepository;
import com.blinkit.application.userAuth.entities.User;
import com.blinkit.application.userAuth.exceptions.UserNotFoundException;
import com.blinkit.application.userAuth.repositories.UserRepository;
import com.blinkit.application.wishlist.dtos.response.WishlistItemResponse;
import com.blinkit.application.wishlist.dtos.response.WishlistResponse;
import com.blinkit.application.wishlist.entities.WishlistItem;
import com.blinkit.application.wishlist.exceptions.ProductAlreadyInWishlistException;
import com.blinkit.application.wishlist.exceptions.WishlistItemNotFoundException;
import com.blinkit.application.wishlist.repositories.WishlistItemRepository;
import com.blinkit.application.wishlist.services.WishlistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public WishlistResponse addItem(Long userId, Long productId) {
        if (wishlistItemRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ProductAlreadyInWishlistException("Product already in wishlist");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        WishlistItem item = new WishlistItem();
        item.setUser(user);
        item.setProduct(product);
        wishlistItemRepository.save(item);

        return getWishlist(userId);
    }

    @Transactional
    public void removeItem(Long userId, Long productId) {
        if (!wishlistItemRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new WishlistItemNotFoundException("Item not in wishlist");
        }
        wishlistItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public WishlistResponse getWishlist(Long userId) {
        List<WishlistItem> items = wishlistItemRepository.findByUserId(userId);

        List<WishlistItemResponse> dtos = items.stream()
                .map(item -> new WishlistItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getSlug(),
                        item.getProduct().getName(),
                        item.getProduct().getImage(),
                        item.getProduct().getMrp(),
                        item.getProduct().getSellingPrice(),
                        item.getProduct().getUnit()
                ))
                .toList();

        return new WishlistResponse(dtos, dtos.size());
    }
}
