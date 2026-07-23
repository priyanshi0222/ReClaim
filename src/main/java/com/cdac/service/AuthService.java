package com.cdac.service;

import com.cdac.dto.request.LoginRequest;
import com.cdac.dto.request.RegisterRequest;
import com.cdac.dto.response.AuthResponse;

public interface AuthService {

    /**
     * Registers a new user.
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates user and returns JWT.
     */
    AuthResponse login(LoginRequest request);

}