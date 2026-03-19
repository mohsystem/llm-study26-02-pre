package com.um.springbootprojstructure.mapper;

import com.um.springbootprojstructure.dto.RegisterResponse;
import com.um.springbootprojstructure.entity.UserAccount;

public class UserAccountMapper {

    private UserAccountMapper() {}

    public static RegisterResponse toRegisterResponse(UserAccount saved) {
        return new RegisterResponse(saved.getId(), "REGISTERED");
    }
}