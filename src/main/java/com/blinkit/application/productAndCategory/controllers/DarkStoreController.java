package com.blinkit.application.productAndCategory.controllers;


import com.blinkit.application.productAndCategory.dtos.requests.DarkStoreRequest;
import com.blinkit.application.productAndCategory.entities.DarkStore;
import com.blinkit.application.productAndCategory.services.DarkStoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class DarkStoreController {

    @Autowired
    private DarkStoreService darkStoreService;

    @GetMapping   // public — needed for the nearest-store lookup on the storefront
    public ResponseEntity<List<DarkStore>> getAll() {
        return ResponseEntity.ok(darkStoreService.getAllActiveDarkStore());
    }

    @PostMapping   // admin
    public ResponseEntity<DarkStore> create(@Valid @RequestBody DarkStoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(darkStoreService.createDarkStore(request));
    }
}