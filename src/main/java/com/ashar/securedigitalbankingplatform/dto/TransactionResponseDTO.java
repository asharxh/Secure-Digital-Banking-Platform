package com.ashar.securedigitalbankingplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {

    private String referenceNumber;

    private String type;

    private Double amount;

    private String senderAccount;

    private String receiverAccount;

    private String description;

    private LocalDateTime timestamp;
}