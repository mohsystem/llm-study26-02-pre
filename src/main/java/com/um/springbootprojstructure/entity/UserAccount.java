package com.um.springbootprojstructure.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_accounts_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_user_accounts_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_user_accounts_public_ref", columnNames = "publicRef")
        }
)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String publicRef = UUID.randomUUID().toString();

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // --- Identity document metadata (optional) ---
    @Column(length = 1024)
    private String documentPath;

    @Column(length = 255)
    private String documentContentType;

    @Column(length = 255)
    private String documentOriginalFilename;

    public UserAccount() {}

    public Long getId() {
        return id;
    }

    public String getPublicRef() {
        return publicRef;
    }

    public void setPublicRef(String publicRef) {
        this.publicRef = publicRef;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public String getDocumentOriginalFilename() {
        return documentOriginalFilename;
    }

    public void setDocumentOriginalFilename(String documentOriginalFilename) {
        this.documentOriginalFilename = documentOriginalFilename;
    }

    @Column(length = 30)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean mfaEnabled = false;

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isMfaEnabled() { return mfaEnabled; }
    public void setMfaEnabled(boolean mfaEnabled) { this.mfaEnabled = mfaEnabled; }
}