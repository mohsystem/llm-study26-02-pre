package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.AccountStatus;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponse> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) AccountStatus status
    ) {
        // Basic guardrails
        if (page < 0) page = 0;
        if (size < 1) size = 1;
        if (size > 200) size = 200;

        return userService.listUsers(page, size, role, status);
    }
}