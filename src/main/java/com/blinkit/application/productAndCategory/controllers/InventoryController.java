package com.blinkit.application.productAndCategory.controllers;

import com.blinkit.application.productAndCategory.dtos.requests.InventoryRequest;
import com.blinkit.application.productAndCategory.entities.Inventory;
import com.blinkit.application.productAndCategory.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> setStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.setStock(request));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<List<Inventory>> getByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(inventoryService.getInventoryByStoreId(storeId));
    }
}