package com.miuky.ewallet.controller;

import com.miuky.ewallet.dto.request.DepositRequest;
import com.miuky.ewallet.dto.request.TransferRequest;
import com.miuky.ewallet.dto.request.WithdrawRequest;
import com.miuky.ewallet.dto.response.ApiResponse;
import com.miuky.ewallet.dto.response.WalletResponse;
import com.miuky.ewallet.security.CustomUserDetails;
import com.miuky.ewallet.service.impl.TransactionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor @Slf4j
public class TransactionController {
    private final TransactionServiceImpl transService;

    @PostMapping("/transfer")
    public ApiResponse<WalletResponse> transfer(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid @RequestBody TransferRequest request) {
        WalletResponse res = transService.transfer(userDetails.getId(), request);
        return ApiResponse.success("Transfer successfully", res);
    }

    @PostMapping("/deposit")
    public ApiResponse<WalletResponse> deposit(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid @RequestBody DepositRequest request) {
        log.info("Current User Id: {}", userDetails.getId());
        WalletResponse res = transService.deposit(userDetails.getId(), request);
        return ApiResponse.success("Deposit successfully", res);
    }

    @PostMapping("/withdraw")
    public ApiResponse<WalletResponse> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid @RequestBody WithdrawRequest request) {
        WalletResponse res = transService.withdraw(userDetails.getId(), request);
        return ApiResponse.success("Withdraw successfully", res);
    }
}
