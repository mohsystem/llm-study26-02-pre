package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.entity.UserAccount;

public interface SessionAuthService {
    UserAccount requireUser(String authorizationHeader);
}