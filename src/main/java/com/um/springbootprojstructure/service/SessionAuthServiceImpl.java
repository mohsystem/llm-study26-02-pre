package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.SessionToken;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.repository.SessionTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import com.um.springbootprojstructure.service.exception.UnauthorizedException;

@Service
public class SessionAuthServiceImpl implements SessionAuthService {

    private final SessionTokenRepository sessionTokenRepository;

    public SessionAuthServiceImpl(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
    }


    @Override
    public UserAccount requireUser(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);

        SessionToken session = sessionTokenRepository
                .findByTokenAndExpiresAtAfter(token, Instant.now())
                .orElseThrow(UnauthorizedException::invalidOrExpiredToken);

        return session.getUser();
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw UnauthorizedException.missingAuthHeader();
        }
        String prefix = "Bearer ";
        if (!authorizationHeader.startsWith(prefix)) {
            throw new UnauthorizedException("Authorization must be Bearer token");
        }
        String token = authorizationHeader.substring(prefix.length()).trim();
        if (token.isEmpty()) {
            throw new UnauthorizedException("Empty token");
        }
        return token;
    }
}