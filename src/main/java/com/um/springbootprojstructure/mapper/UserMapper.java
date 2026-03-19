package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.UserAccount;

public class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(UserAccount u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole().name(),
                u.getStatus().name()
        );
    }
}