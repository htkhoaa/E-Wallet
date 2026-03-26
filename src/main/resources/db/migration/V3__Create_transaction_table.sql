CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_ref VARCHAR(100) NOT NULL UNIQUE,
    from_wallet_id BIGINT NULL,
    to_wallet_id BIGINT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
    status ENUM('INITIATED', 'PROCESSING', 'SUCCESS', 'FAILED', 'REVERSED') NOT NULL DEFAULT 'INITIATED',
    error_message VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_positive_amount CHECK (amount > 0),
    FOREIGN KEY (from_wallet_id) REFERENCES wallets(id),
    FOREIGN KEY (to_wallet_id) REFERENCES wallets(id)
) ENGINE=InnoDB;

CREATE INDEX idx_from_wallet ON transactions(from_wallet_id);
CREATE INDEX idx_to_wallet ON transactions(to_wallet_id);