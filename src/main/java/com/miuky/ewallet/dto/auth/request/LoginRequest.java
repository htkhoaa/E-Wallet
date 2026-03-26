package com.miuky.ewallet.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại không đúng định dạng")
        String phoneNumber,

        @NotBlank(message = "Mật khẩu không được để trống")
        String password
) {}
