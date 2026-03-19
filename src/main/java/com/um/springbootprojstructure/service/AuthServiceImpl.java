package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.LoginRequest;
import com.um.springbootprojstructure.dto.LoginResponse;
import com.um.springbootprojstructure.dto.RegisterRequest;
import com.um.springbootprojstructure.dto.RegisterResponse;
import com.um.springbootprojstructure.entity.SessionToken;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.mapper.UserAccountMapper;
import com.um.springbootprojstructure.repository.SessionTokenRepository;
import com.um.springbootprojstructure.repository.UserAccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.um.springbootprojstructure.service.exception.InvalidCredentialsException;
import com.um.springbootprojstructure.service.exception.InvalidCredentialsException;
import com.um.springbootprojstructure.service.exception.NotFoundException;
import com.um.springbootprojstructure.service.exception.InvalidOperationException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import com.um.springbootprojstructure.dto.ChangePasswordRequest;
import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.PendingLogin;
import com.um.springbootprojstructure.repository.PendingLoginRepository;
import java.time.Duration;
import java.time.Instant;
import java.security.SecureRandom;
import java.util.HexFormat;
import com.um.springbootprojstructure.service.exception.DuplicateAccountException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Duration SESSION_TTL = Duration.ofHours(12);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();
    private final PendingLoginRepository pendingLoginRepository;
    private static final Duration MFA_STEP1_TTL = Duration.ofMinutes(5);
//    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
//    private static final HexFormat HEX = HexFormat.of();

    private String generateToken64Hex() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return HEX.formatHex(bytes);
    }

    public AuthServiceImpl(UserAccountRepository userAccountRepository,
                           SessionTokenRepository sessionTokenRepository,
                           PendingLoginRepository pendingLoginRepository,
                           PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.sessionTokenRepository = sessionTokenRepository;
        this.pendingLoginRepository = pendingLoginRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        userAccountRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw DuplicateAccountException.usernameTaken();
        });
        userAccountRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw DuplicateAccountException.emailTaken();
        });

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        try {
            UserAccount saved = userAccountRepository.save(user);
            return UserAccountMapper.toRegisterResponse(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // race condition: map deterministically
            throw new DuplicateAccountException("DUPLICATE_ACCOUNT", "Username or email already exists");
        }
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        String identifier = request.getIdentifier().trim();

        UserAccount user = userAccountRepository.findByUsernameIgnoreCase(identifier)
                .or(() -> userAccountRepository.findByEmailIgnoreCase(identifier))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        if (user.isMfaEnabled()) {
            PendingLogin pl = new PendingLogin();
            pl.setUser(user);
            pl.setMfaToken(generateToken64Hex());
            pl.setExpiresAt(Instant.now().plus(MFA_STEP1_TTL));
            pl.setCompleted(false);
            pendingLoginRepository.save(pl);

            return new LoginResponse(null, null, user.getId(), "MFA_REQUIRED", pl.getMfaToken());
        }

        // no MFA -> create session token like before
        String token = generateToken64Hex();
        SessionToken sessionToken = new SessionToken();
        sessionToken.setToken(token);
        sessionToken.setUser(user);
        sessionToken.setExpiresAt(Instant.now().plus(Duration.ofHours(12)));
        sessionTokenRepository.save(sessionToken);

        return new LoginResponse(token, "SESSION", user.getId(), "AUTHENTICATED", null);
    }

//    private String generateToken64Hex() {
//        byte[] bytes = new byte[32]; // 256-bit => 64 hex chars
//        SECURE_RANDOM.nextBytes(bytes);
//        return HEX.formatHex(bytes);
//    }

    @Override
    @Transactional
    public StatusResponse changePassword(Long userId, ChangePasswordRequest request) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            // deterministic: wrong current password => unauthorized
            throw new InvalidCredentialsException();
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new InvalidOperationException("PASSWORD_REUSE_NOT_ALLOWED", "New password must be different from current password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userAccountRepository.save(user);

        return new StatusResponse("PASSWORD_CHANGED");
    }

}