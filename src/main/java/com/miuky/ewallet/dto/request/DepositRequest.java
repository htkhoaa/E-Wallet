package com.miuky.ewallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull(message = "Số tiền không được để trống")
        @DecimalMin(value = "10000.0", message = "Số tiền nạp tối thiểu là 10.000 VNĐ")
        BigDecimal amount,

        @NotBlank(message = "Mã tham chiếu giao dịch không được để trống")
        String transactionRef
) {}
