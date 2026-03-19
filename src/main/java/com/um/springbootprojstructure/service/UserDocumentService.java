package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.StatusResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UserDocumentService {

    record DocumentResource(Resource resource,
                            String contentType,
                            String filename) {}

    DocumentResource getIdentityDocumentByPublicRef(String publicRef);

    // Add this:
    StatusResponse uploadIdentityDocument(String publicRef, MultipartFile file);
}