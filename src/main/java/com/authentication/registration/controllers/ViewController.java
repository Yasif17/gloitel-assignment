package com.authentication.registration.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/register-page")
    public String showRegisterPage() {
        return "register"; // resolves to /WEB-INF/views/register.jsp
    }

    @GetMapping("/login-page")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/home-page")
    public String home(){
        return "home";
    }

    @GetMapping("/admin-page")
    public String admin(){
        return "admin";
    }

}