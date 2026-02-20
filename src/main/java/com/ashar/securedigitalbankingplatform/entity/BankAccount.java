package com.ashar.securedigitalbankingplatform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
