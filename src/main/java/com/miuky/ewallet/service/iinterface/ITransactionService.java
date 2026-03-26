package com.miuky.ewallet.service.iinterface;

import com.miuky.ewallet.dto.request.DepositRequest;
import com.miuky.ewallet.dto.request.TransferRequest;
import com.miuky.ewallet.dto.request.WithdrawRequest;
import com.miuky.ewallet.dto.response.WalletResponse;
import com.miuky.ewallet.repository.WalletRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ITransactionService {

    WalletResponse deposit(Long userId, DepositRequest request);
    WalletResponse withdraw(Long userId, WithdrawRequest request);
    WalletResponse transfer(Long userId, TransferRequest request);
}
