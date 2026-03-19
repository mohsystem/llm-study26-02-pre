package com.um.springbootprojstructure.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String status;

    // Add this:
    private String mfaToken;

    public LoginResponse() {}

    public LoginResponse(String token, String tokenType, Long userId, String status) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.status = status;
    }

    public LoginResponse(String token, String tokenType, Long userId, String status, String mfaToken) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.status = status;
        this.mfaToken = mfaToken;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMfaToken() { return mfaToken; }
    public void setMfaToken(String mfaToken) { this.mfaToken = mfaToken; }
}