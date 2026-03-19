package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.ApiKeyCreateRequest;
import com.um.springbootprojstructure.dto.ApiKeyCreateResponse;
import com.um.springbootprojstructure.dto.ApiKeyResponse;
import com.um.springbootprojstructure.dto.StatusResponse;

import java.util.List;

public interface ApiKeyService {
    ApiKeyCreateResponse issueKey(Long userId, ApiKeyCreateRequest request);
    List<ApiKeyResponse> listKeys(Long userId);
    StatusResponse revokeKey(Long userId, Long keyId);
}