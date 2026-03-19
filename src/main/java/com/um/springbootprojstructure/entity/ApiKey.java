package com.um.springbootprojstructure.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "api_keys",
        indexes = {
                @Index(name = "idx_api_keys_user_id", columnList = "user_id"),
                @Index(name = "idx_api_keys_key_hash", columnList = "keyHash", unique = true)
        }
)
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_api_keys_user"))
    private UserAccount user;

    @Column(nullable = false, unique = true, length = 64)
    private String keyHash;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    private Instant expiresAt;

    private Instant revokedAt;

    public ApiKey() {}

    public Long getId() { return id; }

    public UserAccount getUser() { return user; }
    public void setUser(UserAccount user) { this.user = user; }

    public String getKeyHash() { return keyHash; }
    public void setKeyHash(String keyHash) { this.keyHash = keyHash; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public Instant getRevokedAt() { return revokedAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }

    @Transient
    public boolean isActive() {
        if (revokedAt != null) return false;
        return expiresAt == null || expiresAt.isAfter(Instant.now());
    }
}