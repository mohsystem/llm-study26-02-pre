package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.ApiKeyCreateRequest;
import com.um.springbootprojstructure.dto.ApiKeyCreateResponse;
import com.um.springbootprojstructure.dto.ApiKeyResponse;
import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.ApiKey;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.mapper.ApiKeyMapper;
import com.um.springbootprojstructure.repository.ApiKeyRepository;
import com.um.springbootprojstructure.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserAccountRepository userAccountRepository;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository,
                             UserAccountRepository userAccountRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse issueKey(Long userId, ApiKeyCreateRequest request) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String rawKey = generateRawApiKey();
        String keyHash = sha256Hex(rawKey);

        ApiKey apiKey = new ApiKey();
        apiKey.setUser(user);
        apiKey.setName(request.getName().trim());
        apiKey.setKeyHash(keyHash);

        if (request.getExpiresAtEpochMillis() != null) {
            apiKey.setExpiresAt(Instant.ofEpochMilli(request.getExpiresAtEpochMillis()));
        }

        apiKeyRepository.save(apiKey);

        // Return raw key only now (cannot be retrieved later)
        return new ApiKeyCreateResponse(apiKey.getId(), "API_KEY_ISSUED", rawKey);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKeyResponse> listKeys(Long userId) {
        return apiKeyRepository.findAllByUserIdOrderByIdDesc(userId)
                .stream()
                .map(ApiKeyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public StatusResponse revokeKey(Long userId, Long keyId) {
        ApiKey apiKey = apiKeyRepository.findByIdAndUserId(keyId, userId)
                .orElseThrow(() -> new IllegalArgumentException("API key not found"));

        if (apiKey.getRevokedAt() == null) {
            apiKey.setRevokedAt(Instant.now());
            apiKeyRepository.save(apiKey);
        }

        return new StatusResponse("API_KEY_REVOKED");
    }

    private String generateRawApiKey() {
        // Suggested format: base64url(32 random bytes) => ~43 chars
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Hex(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HEX.formatHex(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to hash API key", e);
        }
    }
}