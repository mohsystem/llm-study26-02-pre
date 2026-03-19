package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.LdapUserResponse;
import com.um.springbootprojstructure.service.DirectoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/directory")
public class AdminDirectoryController {

    private final DirectoryService directoryService;

    public AdminDirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/user-search")
    public List<LdapUserResponse> userSearch(
            @RequestParam String dc,
            @RequestParam String username
    ) {
        return directoryService.searchUser(dc, username);
    }
}