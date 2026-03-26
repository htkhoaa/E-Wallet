package com.miuky.ewallet.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$",
                message = "Số điện thoại không hợp lệ (Phải là số VN bắt đầu bằng 03, 05, 07, 08, 09)")
        String phoneNumber,

        @NotBlank(message = "Họ và tên không được để trống")
        @Size(min = 2, max = 100, message = "Họ và tên phải từ 2 đến 100 ký tự")
        String fullName,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "Mật khẩu phải chứa ít nhất 1 chữ cái và 1 chữ số để đảm bảo an toàn")
        String password
) {}
