package com.miuky.ewallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "ID của người nhận không được trống")
        Long toUserId,

        @NotNull(message = "Số tiền không được để trống")
        @DecimalMin(value = "1000.0", message = "Số tiền rút tối thiểu là 10.000 VNĐ")
        BigDecimal amount,

        @NotBlank(message = "Mã tham chiếu giao dịch không được để trống")
        String transactionRef,

        String description
) {}
