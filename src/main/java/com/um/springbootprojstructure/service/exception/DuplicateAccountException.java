package com.um.springbootprojstructure.service.exception;

public class DuplicateAccountException extends ApiException {
    public DuplicateAccountException(String code, String message) {
        super(code, message);
    }

    public static DuplicateAccountException usernameTaken() {
        return new DuplicateAccountException("USERNAME_TAKEN", "Username is already taken");
    }

    public static DuplicateAccountException emailTaken() {
        return new DuplicateAccountException("EMAIL_TAKEN", "Email is already registered");
    }
}