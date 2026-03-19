package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.ChangePasswordRequest;
import com.um.springbootprojstructure.dto.LoginRequest;
import com.um.springbootprojstructure.dto.LoginResponse;
import com.um.springbootprojstructure.dto.RegisterRequest;
import com.um.springbootprojstructure.dto.RegisterResponse;
import com.um.springbootprojstructure.dto.ResetRequest;
import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.service.AuthService;
import com.um.springbootprojstructure.service.PasswordResetService;
import com.um.springbootprojstructure.service.SessionAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.um.springbootprojstructure.dto.ResetConfirmRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SessionAuthService sessionAuthService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService,
                          SessionAuthService sessionAuthService,
                          PasswordResetService passwordResetService) {
        this.authService = authService;
        this.sessionAuthService = sessionAuthService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/reset-confirm")
    public StatusResponse resetConfirm(@Valid @RequestBody ResetConfirmRequest request) {
        String status = passwordResetService.confirmReset(request.getToken(), request.getNewPassword());
        return new StatusResponse(status);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/change-password")
    public StatusResponse changePassword(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        UserAccount currentUser = sessionAuthService.requireUser(authorization);
        return authService.changePassword(currentUser.getId(), request);
    }

    @PostMapping("/reset-request")
    public StatusResponse resetRequest(@Valid @RequestBody ResetRequest request) {
        String status = passwordResetService.initiateReset(request.getIdentifier());
        return new StatusResponse(status);
    }
}