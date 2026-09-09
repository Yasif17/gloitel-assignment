package com.authentication.registration.controllers;

import com.authentication.registration.dtos.request.LoginRequest;
import com.authentication.registration.dtos.request.RegisterRequest;
import com.authentication.registration.dtos.response.AuthResponse;
import com.authentication.registration.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path="/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping(path="/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(path="/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

}
