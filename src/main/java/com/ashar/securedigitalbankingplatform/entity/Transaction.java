package com.ashar.securedigitalbankingplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private Double amount;

    private String status;

    private String referenceNumber;

    private String senderAccount;

    private String receiverAccount;

    private String description;

    private LocalDateTime timestamp;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = "SUCCESS";
        }

        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    @Enumerated(EnumType.STRING)
    private TransactionCategory category;
}