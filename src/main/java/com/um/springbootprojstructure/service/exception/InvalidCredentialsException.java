package com.um.springbootprojstructure.service.exception;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super("INVALID_CREDENTIALS", "Invalid credentials");
    }
}