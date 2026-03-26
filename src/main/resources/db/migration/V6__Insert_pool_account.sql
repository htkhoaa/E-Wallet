INSERT INTO users (id, phone_number, password_hash, pin_hash, full_name, email, kyc_status, identity_card_number, status)
VALUES (0, 'SYSTEM', 'N/A', 'N/A', 'E_Wallet System',
        'admin@appx.vn','VERIFIED','SYSTEM_000','ACTIVE');

INSERT INTO wallets (id, user_id, balance, currency, status)
VALUES (1, 1, 0.0000,'VND', 'ACTIVE');