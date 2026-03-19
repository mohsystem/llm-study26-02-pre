package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.MfaChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface MfaChallengeRepository extends JpaRepository<MfaChallenge, Long> {

    Optional<MfaChallenge> findByMfaTokenAndVerifiedFalseAndExpiresAtAfter(String mfaToken, Instant now);
}