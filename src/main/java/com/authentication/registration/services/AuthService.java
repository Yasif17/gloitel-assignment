package com.authentication.registration.services;

import com.authentication.registration.dtos.request.LoginRequest;
import com.authentication.registration.dtos.request.RegisterRequest;
import com.authentication.registration.dtos.response.AuthResponse;

public interface AuthService {

    public AuthResponse register(RegisterRequest request);

    public AuthResponse login(LoginRequest request);

    public void logout(String token);

//    public void blacklistToken(String token);

}
