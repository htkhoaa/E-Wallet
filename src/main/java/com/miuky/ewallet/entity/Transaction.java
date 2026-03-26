package com.miuky.ewallet.entity;

import com.miuky.ewallet.common.TransactionStatus;
import com.miuky.ewallet.common.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Table(name = "transactions")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Transaction extends BaseEntity {
    @Column(name = "transaction_ref", nullable = false, unique = true, length = 100)
    private String transactionRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_wallet_id")
    private Wallet fromWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_wallet_id")
    private Wallet toWallet;

    @Column(precision = 19, scale = 4, nullable = false) @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING) @Builder.Default
    private TransactionStatus status = TransactionStatus.INITIATED;

    @Column(name = "error_message")
    private String errorMessage;


}
