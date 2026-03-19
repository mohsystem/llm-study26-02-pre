package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApiKeyCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    /**
     * Optional: epoch millis. If null => no expiry.
     */
    private Long expiresAtEpochMillis;

    public ApiKeyCreateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getExpiresAtEpochMillis() { return expiresAtEpochMillis; }
    public void setExpiresAtEpochMillis(Long expiresAtEpochMillis) { this.expiresAtEpochMillis = expiresAtEpochMillis; }
}