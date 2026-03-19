package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetRequest {

    /**
     * Username OR Email.
     */
    @NotBlank
    @Size(max = 254)
    private String identifier;

    public ResetRequest() {}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}