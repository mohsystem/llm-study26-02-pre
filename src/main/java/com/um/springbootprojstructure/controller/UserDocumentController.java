package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.service.UserDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserDocumentController {

    private final UserDocumentService userDocumentService;

    public UserDocumentController(UserDocumentService userDocumentService) {
        this.userDocumentService = userDocumentService;
    }

    @GetMapping("/{publicRef}/document")
    public ResponseEntity<Resource> getIdentityDocument(@PathVariable String publicRef) {
        UserDocumentService.DocumentResource doc =
                userDocumentService.getIdentityDocumentByPublicRef(publicRef);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.filename() + "\"")
                .body(doc.resource());
    }

    @PutMapping(path = "/{publicRef}/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse uploadIdentityDocument(
            @PathVariable String publicRef,
            @RequestPart("file") MultipartFile file
    ) {
        return userDocumentService.uploadIdentityDocument(publicRef, file);
    }
}