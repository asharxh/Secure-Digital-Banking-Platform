package com.ashar.securedigitalbankingplatform.repository;

import com.ashar.securedigitalbankingplatform.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountAccountNumber(String accountNumber);

    List<Transaction> findByAccountAccountNumberAndTimestampBetween(
            String accountNumber,
            LocalDateTime start,
            LocalDateTime end
    );
}