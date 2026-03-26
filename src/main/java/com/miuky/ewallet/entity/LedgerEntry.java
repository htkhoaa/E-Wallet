package com.miuky.ewallet.entity;

import com.miuky.ewallet.common.Direction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Table(name = "ledger_entries")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LedgerEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(precision = 19, scale = 4, nullable = false) @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "post_balance", precision = 19, scale = 4, nullable = false) @Builder.Default
    private BigDecimal postBalance = BigDecimal.ZERO;
}
