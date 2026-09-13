package com.blinkit.application.userAuth.services;

import com.blinkit.application.userAuth.dtos.request.LoginRequest;
import com.blinkit.application.userAuth.dtos.request.RegisterRequest;
import com.blinkit.application.userAuth.dtos.response.AuthResponse;

public interface AuthService {

    public AuthResponse register(RegisterRequest request);

    public AuthResponse login(LoginRequest request);

    public void logout(String token);

//    public void blacklistToken(String token);

}
