package com.ashar.securedigitalbankingplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PendingTransferDTO implements Serializable {

    private String fromAccount;
    private String toAccount;
    private Double amount;
}