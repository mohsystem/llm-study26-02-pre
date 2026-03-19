package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.MfaChallengeResponse;
import com.um.springbootprojstructure.dto.MfaVerifyResponse;

public interface MfaService {
    MfaChallengeResponse challenge(String mfaToken);
    MfaVerifyResponse verify(String mfaToken, String otp);
}