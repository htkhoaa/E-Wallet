package com.miuky.ewallet.entity;

import com.miuky.ewallet.common.KycStatus;
import com.miuky.ewallet.common.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "users")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity{

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "pin_hash")
    private String pinHash;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "kyc_status") @Enumerated(EnumType.STRING) @Builder.Default
    private KycStatus kycStatus = KycStatus.UNVERIFIED;

    @Column(name = "identity_card_number", length = 20, unique = true)
    private String identityCardNumber;

    @Column(name = "status") @Enumerated(EnumType.STRING) @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;
}
