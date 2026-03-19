package com.um.springbootprojstructure.service.exception;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message);
    }

    public static UnauthorizedException missingAuthHeader() {
        return new UnauthorizedException("Missing Authorization header");
    }

    public static UnauthorizedException invalidOrExpiredToken() {
        return new UnauthorizedException("Invalid or expired token");
    }
}