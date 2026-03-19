package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.PasswordResetToken;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.repository.PasswordResetTokenRepository;
import com.um.springbootprojstructure.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;
import com.um.springbootprojstructure.service.exception.InvalidOperationException;


@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Duration RESET_TTL = Duration.ofMinutes(30);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();

    public PasswordResetServiceImpl(UserAccountRepository userAccountRepository,
                                    PasswordResetTokenRepository passwordResetTokenRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public String initiateReset(String identifierRaw) {
        String identifier = identifierRaw == null ? "" : identifierRaw.trim();

        Optional<UserAccount> userOpt =
                userAccountRepository.findByUsernameIgnoreCase(identifier)
                        .or(() -> userAccountRepository.findByEmailIgnoreCase(identifier));

        userOpt.ifPresent(user -> {
            PasswordResetToken prt = new PasswordResetToken();
            prt.setUser(user);
            prt.setToken(generateToken64Hex());
            prt.setExpiresAt(Instant.now().plus(RESET_TTL));
            prt.setUsed(false);
            passwordResetTokenRepository.save(prt);

            // In real system: email the token/link to user.
        });

        return "RESET_REQUESTED";
    }

    @Override
    @Transactional
    public String confirmReset(String tokenRaw, String newPassword) {
        String token = tokenRaw == null ? "" : tokenRaw.trim();

        PasswordResetToken prt = passwordResetTokenRepository
                .findByTokenAndUsedFalseAndExpiresAtAfter(token, Instant.now())
                .orElseThrow(() -> new InvalidOperationException("RESET_TOKEN_INVALID", "Invalid or expired reset token"));

        UserAccount user = prt.getUser();

        // Optional: prevent setting the same password again
        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user);

        prt.setUsed(true);
        passwordResetTokenRepository.save(prt);

        return "PASSWORD_RESET";
    }

    private String generateToken64Hex() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return HEX.formatHex(bytes);
    }
}