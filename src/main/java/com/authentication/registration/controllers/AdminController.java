package com.authentication.registration.controllers;

import com.authentication.registration.dtos.request.RoleUpdateRequest;
import com.authentication.registration.dtos.response.UserSummaryDto;
import com.authentication.registration.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/users")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping(path = "/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        adminService.updateRole(id, request.getRole());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserSummaryDto> partialUpdate(@PathVariable Long id, @RequestBody UserSummaryDto userDto) {
        return ResponseEntity.ok(adminService.patchUpdate(id, userDto));
    }
}
