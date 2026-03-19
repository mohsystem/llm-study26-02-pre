package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.PendingLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface PendingLoginRepository extends JpaRepository<PendingLogin, Long> {

    Optional<PendingLogin> findByMfaTokenAndCompletedFalseAndExpiresAtAfter(String mfaToken, Instant now);
}