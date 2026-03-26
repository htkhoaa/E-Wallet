package com.miuky.ewallet.dto.auth.response;

public record AuthResponse(
        String token,
        Long userId,
        String fullName
){}
