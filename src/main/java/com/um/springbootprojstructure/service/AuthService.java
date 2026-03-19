package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.ChangePasswordRequest;
import com.um.springbootprojstructure.dto.LoginRequest;
import com.um.springbootprojstructure.dto.LoginResponse;
import com.um.springbootprojstructure.dto.RegisterRequest;
import com.um.springbootprojstructure.dto.RegisterResponse;
import com.um.springbootprojstructure.dto.StatusResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);

    // Add this:
    StatusResponse changePassword(Long userId, ChangePasswordRequest request);
}