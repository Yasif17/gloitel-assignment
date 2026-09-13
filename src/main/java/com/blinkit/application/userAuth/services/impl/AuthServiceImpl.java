package com.blinkit.application.userAuth.services.impl;

import com.blinkit.application.userAuth.dtos.request.LoginRequest;
import com.blinkit.application.userAuth.dtos.request.RegisterRequest;
import com.blinkit.application.userAuth.dtos.response.AuthResponse;
import com.blinkit.application.userAuth.entities.User;
import com.blinkit.application.userAuth.enums.Role;
import com.blinkit.application.userAuth.exceptions.InvalidCredentialsException;
import com.blinkit.application.userAuth.exceptions.UserAlreadyExistException;
import com.blinkit.application.userAuth.repositories.UserRepository;
import com.blinkit.application.userAuth.security.JwtUtil;
import com.blinkit.application.userAuth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistException("User is already exists.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user.getId(),user.getEmail(),user.getRole().name());

        return new AuthResponse(accessToken,"Bearer",user.getId(),user.getName(),user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->{
            throw new InvalidCredentialsException("Invalid email or password");
        });

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(),user.getEmail(),user.getRole().name());
        return new AuthResponse(accessToken,"Bearer",user.getId(),user.getName(),user.getRole().name());
    }

    @Override
    public void logout(String token) {
        long remainingTime = jwtUtil.remainingValidityMs(token);
        if(remainingTime>0){
            redisTemplate.opsForValue().set("blacklist:" + token,"true", Duration.ofMillis(remainingTime));
        }
    }

//    @Override
//    public void blacklistToken(String token) {
//        long remainingTime = jwtUtil.remainingValidityMs(token);
//        if(remainingTime>0){
//            redisTemplate.opsForValue().set("blacklist:" + token,"true", Duration.ofMillis(remainingTime));
//        }
//    }


}
