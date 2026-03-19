package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.MfaChallengeRequest;
import com.um.springbootprojstructure.dto.MfaChallengeResponse;
import com.um.springbootprojstructure.dto.MfaVerifyRequest;
import com.um.springbootprojstructure.dto.MfaVerifyResponse;
import com.um.springbootprojstructure.service.MfaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/mfa")
public class MfaController {

    private final MfaService mfaService;

    public MfaController(MfaService mfaService) {
        this.mfaService = mfaService;
    }

    @PostMapping("/challenge")
    public MfaChallengeResponse challenge(@Valid @RequestBody MfaChallengeRequest request) {
        return mfaService.challenge(request.getMfaToken());
    }

    @PostMapping("/verify")
    public MfaVerifyResponse verify(@Valid @RequestBody MfaVerifyRequest request) {
        return mfaService.verify(request.getMfaToken(), request.getOtp());
    }
}