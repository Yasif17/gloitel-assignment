package com.authentication.registration.authRegister.exceptions;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(){}

    public UserAlreadyExistException(String message) {
        super(message);
    }

}
