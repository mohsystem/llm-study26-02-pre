package com.um.springbootprojstructure.service.exception;

public class InvalidOperationException extends ApiException {
    public InvalidOperationException(String code, String message) {
        super(code, message);
    }
}