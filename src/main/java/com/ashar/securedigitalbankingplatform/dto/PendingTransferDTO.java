package com.ashar.securedigitalbankingplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PendingTransferDTO {

    private String fromAccount;
    private String toAccount;
    private Double amount;
}