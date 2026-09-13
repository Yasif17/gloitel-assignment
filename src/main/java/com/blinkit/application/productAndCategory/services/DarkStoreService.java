package com.blinkit.application.productAndCategory.services;

import com.blinkit.application.productAndCategory.dtos.requests.DarkStoreRequest;
import com.blinkit.application.productAndCategory.entities.DarkStore;

import java.util.List;

public interface DarkStoreService {

    public List<DarkStore> getAllActiveDarkStore();

    public DarkStore createDarkStore(DarkStoreRequest darkStoreRequest);

}
