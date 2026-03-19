package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.ApiKeyResponse;
import com.um.springbootprojstructure.entity.ApiKey;

import java.time.Instant;

public class ApiKeyMapper {

    private ApiKeyMapper() {}

    public static ApiKeyResponse toResponse(ApiKey k) {
        String status;
        if (k.getRevokedAt() != null) {
            status = "REVOKED";
        } else if (k.getExpiresAt() != null && k.getExpiresAt().isBefore(Instant.now())) {
            status = "EXPIRED";
        } else {
            status = "ACTIVE";
        }

        return new ApiKeyResponse(
                k.getId(),
                k.getName(),
                status,
                k.getCreatedAt() == null ? null : k.getCreatedAt().toEpochMilli(),
                k.getExpiresAt() == null ? null : k.getExpiresAt().toEpochMilli(),
                k.getRevokedAt() == null ? null : k.getRevokedAt().toEpochMilli()
        );
    }
}