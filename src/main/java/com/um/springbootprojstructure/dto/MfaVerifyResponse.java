package com.um.springbootprojstructure.dto;

public class MfaVerifyResponse {

    private String status;
    private String token;
    private String tokenType;
    private Long userId;

    public MfaVerifyResponse() {}

    public MfaVerifyResponse(String status, String token, String tokenType, Long userId) {
        this.status = status;
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}