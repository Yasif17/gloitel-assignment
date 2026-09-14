package com.blinkit.application.wishlist.controllers;

import com.blinkit.application.userAuth.exceptions.UserNotFoundException;
import com.blinkit.application.userAuth.repositories.UserRepository;
import com.blinkit.application.wishlist.dtos.response.WishlistResponse;
import com.blinkit.application.wishlist.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserRepository userRepository;

    private Long currentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }

    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlist(currentUserId()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishlistResponse> addItem(@PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addItem(currentUserId(), productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId) {
        wishlistService.removeItem(currentUserId(), productId);
        return ResponseEntity.noContent().build();
    }
}