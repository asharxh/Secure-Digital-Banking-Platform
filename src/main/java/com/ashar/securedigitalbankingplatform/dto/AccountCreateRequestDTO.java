package com.ashar.securedigitalbankingplatform.dto;

import com.ashar.securedigitalbankingplatform.entity.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreateRequestDTO {

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}