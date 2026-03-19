package com.um.springbootprojstructure.dto;

public class ApiKeyCreateResponse {

    private Long id;
    private String status;
    private String apiKey; // raw key returned only on creation

    public ApiKeyCreateResponse() {}

    public ApiKeyCreateResponse(Long id, String status, String apiKey) {
        this.id = id;
        this.status = status;
        this.apiKey = apiKey;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}