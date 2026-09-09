package com.authentication.registration.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    InvalidCredentialsException(){}

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
