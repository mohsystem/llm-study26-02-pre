package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.ApiKeyCreateRequest;
import com.um.springbootprojstructure.dto.ApiKeyCreateResponse;
import com.um.springbootprojstructure.dto.ApiKeyResponse;
import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.service.ApiKeyService;
import com.um.springbootprojstructure.service.SessionAuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final SessionAuthService sessionAuthService;

    public ApiKeyController(ApiKeyService apiKeyService, SessionAuthService sessionAuthService) {
        this.apiKeyService = apiKeyService;
        this.sessionAuthService = sessionAuthService;
    }

    @PostMapping
    public ApiKeyCreateResponse issue(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody ApiKeyCreateRequest request
    ) {
        UserAccount user = sessionAuthService.requireUser(authorization);
        return apiKeyService.issueKey(user.getId(), request);
    }

    @GetMapping
    public List<ApiKeyResponse> list(
            @RequestHeader(name = "Authorization", required = false) String authorization
    ) {
        UserAccount user = sessionAuthService.requireUser(authorization);
        return apiKeyService.listKeys(user.getId());
    }

    @DeleteMapping("/{keyId}")
    public StatusResponse revoke(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable Long keyId
    ) {
        UserAccount user = sessionAuthService.requireUser(authorization);
        return apiKeyService.revokeKey(user.getId(), keyId);
    }
}