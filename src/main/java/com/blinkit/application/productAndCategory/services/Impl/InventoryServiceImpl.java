package com.blinkit.application.productAndCategory.services.Impl;

import com.blinkit.application.productAndCategory.dtos.requests.InventoryRequest;
import com.blinkit.application.productAndCategory.entities.DarkStore;
import com.blinkit.application.productAndCategory.entities.Inventory;
import com.blinkit.application.productAndCategory.entities.Product;
import com.blinkit.application.productAndCategory.exceptions.DarkStoreNotFoundException;
import com.blinkit.application.productAndCategory.exceptions.ProductNotFoundException;
import com.blinkit.application.productAndCategory.repositories.DarkStoreRepository;
import com.blinkit.application.productAndCategory.repositories.InventoryRepository;
import com.blinkit.application.productAndCategory.repositories.ProductRepository;
import com.blinkit.application.productAndCategory.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private DarkStoreRepository darkStoreRepository;

    // Handles both "first time stocking this product" and "restocking" in one call
    public Inventory setStock(InventoryRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        DarkStore store = darkStoreRepository.findById(request.getDarkStoreId())
                .orElseThrow(() -> new DarkStoreNotFoundException("Dark store not found"));

        Inventory inventory = inventoryRepository
                .findByProductIdAndDarkStoreId(request.getProductId(), request.getDarkStoreId())
                .orElseGet(Inventory::new);   // create new row if none exists yet

        inventory.setProduct(product);
        inventory.setDarkStore(store);
        inventory.setQuantity(request.getQuantity());
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getInventoryByStoreId(Long storeId) {
        return inventoryRepository.findByDarkStoreId(storeId);
    }
}
