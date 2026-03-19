package com.um.springbootprojstructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MfaVerifyRequest {

    @NotBlank
    @Size(min = 64, max = 64)
    private String mfaToken;

    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$")
    private String otp;

    public MfaVerifyRequest() {}

    public String getMfaToken() { return mfaToken; }
    public void setMfaToken(String mfaToken) { this.mfaToken = mfaToken; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}