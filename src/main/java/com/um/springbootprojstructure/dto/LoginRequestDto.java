package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    // Can be either username or email
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;

    public LoginRequestDto() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
