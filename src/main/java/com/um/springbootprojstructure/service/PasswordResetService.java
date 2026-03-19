package com.um.springbootprojstructure.service;

public interface PasswordResetService {
    String initiateReset(String identifier);
    // Add this:
    String confirmReset(String token, String newPassword);
}