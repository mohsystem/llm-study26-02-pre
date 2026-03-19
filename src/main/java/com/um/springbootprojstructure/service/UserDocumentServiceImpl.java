package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.StorageProperties;
import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.repository.UserAccountRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.Set;

@Service
public class UserDocumentServiceImpl implements UserDocumentService {

    private final UserAccountRepository userAccountRepository;
    private final Path storageDir;

    // Adjust allowed types as you need
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    public UserDocumentServiceImpl(UserAccountRepository userAccountRepository,
                                   StorageProperties storageProperties) {
        this.userAccountRepository = userAccountRepository;
        this.storageDir = Path.of(storageProperties.getIdentityDocumentsDir()).toAbsolutePath().normalize();
    }

    @Override
    public DocumentResource getIdentityDocumentByPublicRef(String publicRef) {
        UserAccount user = userAccountRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getDocumentPath() == null || user.getDocumentPath().isBlank()) {
            throw new IllegalArgumentException("Document not found");
        }

        Path path = Path.of(user.getDocumentPath()).normalize();
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Document not found");
        }

        Resource resource = new FileSystemResource(path);

        String contentType = user.getDocumentContentType();
        if (contentType == null || contentType.isBlank()) {
            try {
                contentType = Files.probeContentType(path);
            } catch (Exception ignored) {}
        }
        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        String filename = user.getDocumentOriginalFilename();
        if (filename == null || filename.isBlank()) {
            filename = path.getFileName().toString();
        }

        return new DocumentResource(resource, contentType, filename);
    }

    @Override
    @Transactional
    public StatusResponse uploadIdentityDocument(String publicRef, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported content type");
        }

        UserAccount user = userAccountRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            Files.createDirectories(storageDir);

            // Delete previous file if present
            deleteExistingIfAny(user);

            String originalName = StringUtils.cleanPath(
                    file.getOriginalFilename() == null ? "document" : file.getOriginalFilename()
            );

            String ext = extensionFrom(originalName);
            String storedFileName = user.getPublicRef() + "-" + Instant.now().toEpochMilli() + (ext.isBlank() ? "" : "." + ext);

            Path target = storageDir.resolve(storedFileName).normalize();

            // Safety check: prevent path traversal
            if (!target.startsWith(storageDir)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            user.setDocumentPath(target.toString());
            user.setDocumentContentType(contentType);
            user.setDocumentOriginalFilename(originalName);
            userAccountRepository.save(user);

            return new StatusResponse("DOCUMENT_UPDATED");
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to store document");
        }
    }

    private void deleteExistingIfAny(UserAccount user) {
        String existing = user.getDocumentPath();
        if (existing == null || existing.isBlank()) return;

        try {
            Path p = Path.of(existing).normalize();
            if (Files.exists(p) && Files.isRegularFile(p)) {
                Files.delete(p);
            }
        } catch (Exception ignored) {
            // If delete fails, continue; you may want to log and/or fail depending on requirements.
        }
    }

    private String extensionFrom(String filename) {
        String name = filename.trim();
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0 || lastDot == name.length() - 1) return "";
        String ext = name.substring(lastDot + 1);
        // keep it simple and safe
        ext = ext.replaceAll("[^a-zA-Z0-9]", "");
        return ext.length() > 10 ? ext.substring(0, 10) : ext;
    }
}