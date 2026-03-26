package com.miuky.ewallet.controller;


import com.miuky.ewallet.dto.auth.request.LoginRequest;
import com.miuky.ewallet.dto.auth.request.RegisterRequest;
import com.miuky.ewallet.dto.auth.response.AuthResponse;
import com.miuky.ewallet.dto.response.ApiResponse;
import com.miuky.ewallet.service.auth.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success("Register successfully", authService.register(request));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("Login successfully", authService.login(request));
    }
}
