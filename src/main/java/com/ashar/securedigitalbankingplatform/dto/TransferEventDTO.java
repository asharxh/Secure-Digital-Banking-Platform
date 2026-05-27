package com.ashar.securedigitalbankingplatform.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferEventDTO {

    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String userEmail;
    private String status;
    private String timestamp;
}