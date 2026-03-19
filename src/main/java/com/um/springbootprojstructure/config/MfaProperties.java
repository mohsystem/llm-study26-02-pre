package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.mfa")
public class MfaProperties {
    private long otpTtlSeconds = 300;
    private int maxAttempts = 5;

    public long getOtpTtlSeconds() { return otpTtlSeconds; }
    public void setOtpTtlSeconds(long otpTtlSeconds) { this.otpTtlSeconds = otpTtlSeconds; }

    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
}