package com.blinkit.application.productAndCategory.services;

import com.blinkit.application.productAndCategory.dtos.requests.InventoryRequest;
import com.blinkit.application.productAndCategory.entities.Inventory;

import java.util.List;

public interface InventoryService {

    public Inventory setStock(InventoryRequest inventoryRequest);

    public List<Inventory> getInventoryByStoreId(Long storeId);

}
