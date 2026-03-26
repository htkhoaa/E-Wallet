package com.miuky.ewallet.entity;

import com.miuky.ewallet.common.UserStatus;
import com.miuky.ewallet.common.WalletStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Table(name = "wallets")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Wallet extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder.Default @Column(nullable = false, length = 3)
    private String currency = "VND";

    @Column(name = "status") @Enumerated(EnumType.STRING) @Builder.Default
    private WalletStatus walletStatus = WalletStatus.ACTIVE;
}
