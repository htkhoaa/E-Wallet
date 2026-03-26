CREATE TABLE ledger_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    wallet_id BIGINT NOT NULL,
    direction ENUM('DEBIT', 'CREDIT') NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    post_balance DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES transactions(id),
    FOREIGN KEY (wallet_id) REFERENCES wallets(id)
) ENGINE=InnoDB;