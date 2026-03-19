package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MfaChallengeRequest {

    /**
     * Token proving step-1 authentication succeeded (issued by /login when MFA is enabled).
     */
    @NotBlank
    @Size(min = 64, max = 64)
    private String mfaToken;

    public MfaChallengeRequest() {}

    public String getMfaToken() { return mfaToken; }
    public void setMfaToken(String mfaToken) { this.mfaToken = mfaToken; }
}