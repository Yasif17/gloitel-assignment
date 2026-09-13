package com.blinkit.application.productAndCategory.services.Impl;

import com.blinkit.application.productAndCategory.dtos.requests.DarkStoreRequest;
import com.blinkit.application.productAndCategory.entities.DarkStore;
import com.blinkit.application.productAndCategory.repositories.DarkStoreRepository;
import com.blinkit.application.productAndCategory.services.DarkStoreService;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DarkStoreServiceImpl implements DarkStoreService {

    @Autowired
    private DarkStoreRepository darkStoreRepository;

    public List<DarkStore> getAllActiveDarkStore() {
        return darkStoreRepository.findByActiveTrue();
    }

    public DarkStore createDarkStore(DarkStoreRequest request) {
        DarkStore store = new DarkStore();
        store.setStoreName(request.getStoreName());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());
        store.setActive(true);
        return darkStoreRepository.save(store);
    }
}
