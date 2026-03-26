package com.miuky.ewallet.service.impl;

import com.miuky.ewallet.common.Direction;
import com.miuky.ewallet.common.TransactionStatus;
import com.miuky.ewallet.common.TransactionType;
import com.miuky.ewallet.dto.request.DepositRequest;
import com.miuky.ewallet.dto.request.TransferRequest;
import com.miuky.ewallet.dto.request.WithdrawRequest;
import com.miuky.ewallet.dto.response.WalletResponse;
import com.miuky.ewallet.entity.LedgerEntry;
import com.miuky.ewallet.entity.Transaction;
import com.miuky.ewallet.entity.Wallet;
import com.miuky.ewallet.exception.AppException;
import com.miuky.ewallet.exception.ErrorCode;
import com.miuky.ewallet.repository.LedgerEntryRepository;
import com.miuky.ewallet.repository.TransactionRepository;
import com.miuky.ewallet.repository.WalletRepository;
import com.miuky.ewallet.service.iinterface.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service @RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final TransactionRepository transactionRepo;
    private final WalletRepository walletRepo;
    private final LedgerEntryRepository ledgerEntryRepo;
    private static final Long SYSTEM_POOL_WALLET_ID = 1L;

    @Override @Transactional(rollbackFor = Exception.class)
    public WalletResponse deposit(Long userId, DepositRequest request) {
        if (transactionRepo.existsByTransactionRef(request.transactionRef())) {
            throw new AppException(ErrorCode.DUPLICATE_TRANSACTION);
        }

        Wallet userWallet = walletRepo.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (!"ACTIVE".equals(userWallet.getWalletStatus().name())) throw new AppException(ErrorCode.WALLET_LOCKED);

        Wallet poolWallet = walletRepo.findByUserIdForUpdate(SYSTEM_POOL_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.SYSTEM_WALLET_NOT_INITIALIZED));

        userWallet.setBalance(userWallet.getBalance().add(request.amount()));
        poolWallet.setBalance(poolWallet.getBalance().add(request.amount()));

        Transaction newTxn = buildAndSaveTransaction(request.transactionRef(), null, userWallet, request.amount(),
                TransactionType.DEPOSIT, TransactionStatus.SUCCESS);

        savedLedger(newTxn, userWallet, request.amount(), Direction.CREDIT, userWallet.getBalance());
        savedLedger(newTxn, poolWallet, request.amount(), Direction.DEBIT, poolWallet.getBalance());

        return new WalletResponse(userWallet.getBalance());
    }

    @Override @Transactional(rollbackFor = Exception.class)
    public WalletResponse withdraw(Long userId, WithdrawRequest request) {
        Wallet userWallet = walletRepo.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (userWallet.getBalance().compareTo(request.amount()) < 0)
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);

        if (!"ACTIVE".equals(userWallet.getWalletStatus().name())) throw new AppException(ErrorCode.WALLET_LOCKED);

        Wallet poolWallet = walletRepo.findByUserIdForUpdate(SYSTEM_POOL_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.SYSTEM_WALLET_NOT_INITIALIZED));

        userWallet.setBalance(userWallet.getBalance().subtract(request.amount()));
        poolWallet.setBalance(poolWallet.getBalance().subtract(request.amount()));

        Transaction newTxn = buildAndSaveTransaction(request.transactionRef(), userWallet, null, request.amount(),
                        TransactionType.WITHDRAWAL, TransactionStatus.SUCCESS);

        savedLedger(newTxn, userWallet, request.amount(), Direction.DEBIT, userWallet.getBalance());
        savedLedger(newTxn, poolWallet, request.amount(), Direction.CREDIT, poolWallet.getBalance());

        return new WalletResponse(userWallet.getBalance());
    }

    @Override @Transactional(rollbackFor = Exception.class)
    public WalletResponse transfer(Long fromUserId, TransferRequest request) {
        if (transactionRepo.existsByTransactionRef(request.transactionRef())) {
            throw new AppException(ErrorCode.DUPLICATE_TRANSACTION);
        }

        Long toUserId = request.toUserId();
        if (fromUserId.equals(toUserId)) throw new AppException(ErrorCode.INVALID_TRANSFER);

        Wallet fromWallet = walletRepo.findByUserIdForUpdate(fromUserId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        Wallet toWallet = walletRepo.findByUserIdForUpdate(toUserId)
                .orElseThrow(() -> new AppException(ErrorCode.RECIPIENT_NOT_FOUND));

        if (fromWallet.getBalance().compareTo(request.amount()) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(request.amount()));
        toWallet.setBalance(toWallet.getBalance().add(request.amount()));

        Transaction newTxn = buildAndSaveTransaction(request.transactionRef(), fromWallet, toWallet, request.amount(),
                                                    TransactionType.TRANSFER, TransactionStatus.SUCCESS);

        savedLedger(newTxn, fromWallet, request.amount(), Direction.DEBIT, fromWallet.getBalance());
        savedLedger(newTxn, toWallet, request.amount(), Direction.CREDIT, toWallet.getBalance());

        return new WalletResponse(fromWallet.getBalance());
    }

    private void savedLedger(Transaction txn, Wallet wallet, BigDecimal amount, Direction dir, BigDecimal postBalance) {
        LedgerEntry entry = LedgerEntry.builder()
                .transaction(txn)
                .wallet(wallet)
                .amount(amount)
                .direction(dir)
                .postBalance(postBalance)
                .build();
        ledgerEntryRepo.save(entry);
    }

    private Transaction buildAndSaveTransaction(String transactionRef, Wallet fromWallet, Wallet toWallet, BigDecimal amount,
                                         TransactionType type, TransactionStatus status) {
        Transaction txn = Transaction.builder()
                .transactionRef(transactionRef)
                .fromWallet(fromWallet)
                .toWallet(toWallet)
                .amount(amount)
                .type(type)
                .status(status)
                .build();
        return transactionRepo.save(txn);
    }

}
