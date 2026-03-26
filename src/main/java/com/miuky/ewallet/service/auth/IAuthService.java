package com.miuky.ewallet.service.auth;

import com.miuky.ewallet.dto.auth.request.LoginRequest;
import com.miuky.ewallet.dto.auth.request.RegisterRequest;
import com.miuky.ewallet.dto.auth.response.AuthResponse;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
