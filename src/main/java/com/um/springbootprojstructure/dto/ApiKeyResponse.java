package com.um.springbootprojstructure.dto;

public class ApiKeyResponse {

    private Long id;
    private String name;
    private String status; // ACTIVE / REVOKED / EXPIRED
    private Long createdAtEpochMillis;
    private Long expiresAtEpochMillis;
    private Long revokedAtEpochMillis;

    public ApiKeyResponse() {}

    public ApiKeyResponse(Long id, String name, String status,
                          Long createdAtEpochMillis, Long expiresAtEpochMillis, Long revokedAtEpochMillis) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAtEpochMillis = createdAtEpochMillis;
        this.expiresAtEpochMillis = expiresAtEpochMillis;
        this.revokedAtEpochMillis = revokedAtEpochMillis;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedAtEpochMillis() { return createdAtEpochMillis; }
    public void setCreatedAtEpochMillis(Long createdAtEpochMillis) { this.createdAtEpochMillis = createdAtEpochMillis; }

    public Long getExpiresAtEpochMillis() { return expiresAtEpochMillis; }
    public void setExpiresAtEpochMillis(Long expiresAtEpochMillis) { this.expiresAtEpochMillis = expiresAtEpochMillis; }

    public Long getRevokedAtEpochMillis() { return revokedAtEpochMillis; }
    public void setRevokedAtEpochMillis(Long revokedAtEpochMillis) { this.revokedAtEpochMillis = revokedAtEpochMillis; }
}