package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.session-token")
public class TokenProperties {
    /**
     * Session token time-to-live.
     */
    private Duration ttl = Duration.ofHours(24);

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }
}