package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.MfaProperties;
import com.um.springbootprojstructure.dto.MfaChallengeResponse;
import com.um.springbootprojstructure.dto.MfaVerifyResponse;
import com.um.springbootprojstructure.entity.MfaChallenge;
import com.um.springbootprojstructure.entity.PendingLogin;
import com.um.springbootprojstructure.entity.SessionToken;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.repository.MfaChallengeRepository;
import com.um.springbootprojstructure.repository.PendingLoginRepository;
import com.um.springbootprojstructure.repository.SessionTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class MfaServiceImpl implements MfaService {

    private final PendingLoginRepository pendingLoginRepository;
    private final MfaChallengeRepository mfaChallengeRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final NotificationGatewayClient notificationGatewayClient;
    private final MfaProperties mfaProperties;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();
    private static final Duration SESSION_TTL = Duration.ofHours(12);

    public MfaServiceImpl(PendingLoginRepository pendingLoginRepository,
                          MfaChallengeRepository mfaChallengeRepository,
                          SessionTokenRepository sessionTokenRepository,
                          NotificationGatewayClient notificationGatewayClient,
                          MfaProperties mfaProperties) {
        this.pendingLoginRepository = pendingLoginRepository;
        this.mfaChallengeRepository = mfaChallengeRepository;
        this.sessionTokenRepository = sessionTokenRepository;
        this.notificationGatewayClient = notificationGatewayClient;
        this.mfaProperties = mfaProperties;
    }

    @Override
    @Transactional
    public MfaChallengeResponse challenge(String mfaToken) {
        PendingLogin pending = pendingLoginRepository
                .findByMfaTokenAndCompletedFalseAndExpiresAtAfter(mfaToken, Instant.now())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired MFA token"));

        UserAccount user = pending.getUser();

        if (!user.isMfaEnabled()) {
            throw new IllegalArgumentException("MFA not enabled for user");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("User phone number missing");
        }

        String otp = generateOtp6();

        MfaChallenge ch = new MfaChallenge();
        ch.setUser(user);
        ch.setMfaToken(mfaToken); // reuse same token to link step-1 and OTP
        ch.setOtp(otp);
        ch.setAttempts(0);
        ch.setVerified(false);
        ch.setExpiresAt(Instant.now().plusSeconds(mfaProperties.getOtpTtlSeconds()));
        mfaChallengeRepository.save(ch);

        // Send via external gateway
        notificationGatewayClient.sendSms(user.getPhoneNumber(), "Your verification code is: " + otp);

        return new MfaChallengeResponse("OTP_SENT");
    }

    @Override
    @Transactional
    public MfaVerifyResponse verify(String mfaToken, String otp) {
        PendingLogin pending = pendingLoginRepository
                .findByMfaTokenAndCompletedFalseAndExpiresAtAfter(mfaToken, Instant.now())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired MFA token"));

        MfaChallenge ch = mfaChallengeRepository
                .findByMfaTokenAndVerifiedFalseAndExpiresAtAfter(mfaToken, Instant.now())
                .orElseThrow(() -> new IllegalArgumentException("No active MFA challenge"));

        if (ch.getAttempts() >= mfaProperties.getMaxAttempts()) {
            throw new IllegalArgumentException("Too many attempts");
        }

        ch.setAttempts(ch.getAttempts() + 1);

        if (!ch.getOtp().equals(otp)) {
            mfaChallengeRepository.save(ch);
            throw new IllegalArgumentException("Invalid passcode");
        }

        ch.setVerified(true);
        mfaChallengeRepository.save(ch);

        pending.setCompleted(true);
        pendingLoginRepository.save(pending);

        // Final authentication: issue session token
        UserAccount user = pending.getUser();
        SessionToken st = new SessionToken();
        st.setToken(generateToken64Hex());
        st.setUser(user);
        st.setExpiresAt(Instant.now().plus(SESSION_TTL));
        sessionTokenRepository.save(st);

        return new MfaVerifyResponse("AUTHENTICATED", st.getToken(), "SESSION", user.getId());
    }

    private String generateToken64Hex() {
        byte[] b = new byte[32];
        SECURE_RANDOM.nextBytes(b);
        return HEX.formatHex(b);
    }

    private String generateOtp6() {
        int code = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%06d", code);
    }
}