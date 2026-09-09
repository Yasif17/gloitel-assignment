package com.authentication.registration.controllers;

import com.authentication.registration.dtos.response.UserSummaryDto;
import com.authentication.registration.enums.Role;
import com.authentication.registration.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/admin/users")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserSummaryDto>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping(path="/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestParam Role role){
        adminService.updateRole(id,role);
        return ResponseEntity.noContent().build();
    }

}
