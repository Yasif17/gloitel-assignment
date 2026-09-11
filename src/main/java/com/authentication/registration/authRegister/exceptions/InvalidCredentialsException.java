package com.authentication.registration.authRegister.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    InvalidCredentialsException(){}

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
