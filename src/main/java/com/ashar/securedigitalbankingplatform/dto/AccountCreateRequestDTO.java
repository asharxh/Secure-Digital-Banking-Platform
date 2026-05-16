package com.ashar.securedigitalbankingplatform.dto;

import com.ashar.securedigitalbankingplatform.entity.AccountType;
import lombok.Data;

@Data
public class AccountCreateRequestDTO {

    private AccountType accountType;
}