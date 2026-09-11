package com.authentication.registration.punch.exceptions;

public class PunchNotAllowedException extends RuntimeException {
    public PunchNotAllowedException(String message) {
        super(message);
    }
}
