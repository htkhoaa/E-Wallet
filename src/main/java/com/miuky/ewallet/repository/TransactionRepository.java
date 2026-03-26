package com.miuky.ewallet.repository;

import com.miuky.ewallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByTransactionRef(String transactionRef);
}
