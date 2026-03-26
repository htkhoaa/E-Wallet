package com.miuky.ewallet.exception;

public record ValidationError(
        String field,
        String message
) {}
