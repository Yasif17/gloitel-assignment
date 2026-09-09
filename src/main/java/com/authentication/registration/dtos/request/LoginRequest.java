package com.authentication.registration.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {


    @NotBlank(message="Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message="Password cannot be empty")
    private String password;

}
