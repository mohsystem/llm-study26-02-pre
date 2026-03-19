package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.AccountStatus;
import com.um.springbootprojstructure.entity.Role;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponse> listUsers(int page, int size, Role role, AccountStatus status);
}