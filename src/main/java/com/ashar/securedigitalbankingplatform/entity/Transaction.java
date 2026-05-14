package com.ashar.securedigitalbankingplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Double amount;
    private LocalDateTime timestamp;
    private String referenceNumber;
    private String senderAccount;
    private String receiverAccount;
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;
}