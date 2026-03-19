package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    /**
     * Can be username OR email.
     */
    @NotBlank
    @Size(max = 254)
    private String identifier;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    public LoginRequest() {}

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