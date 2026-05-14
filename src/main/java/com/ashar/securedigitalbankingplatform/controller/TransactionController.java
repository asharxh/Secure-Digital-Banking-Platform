package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.dto.TransactionResponseDTO;
import com.ashar.securedigitalbankingplatform.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/my")
    public List<TransactionResponseDTO> getMyTransactions(
            @RequestParam String accountNumber
    ) {

        return transactionService
                .getMyTransactions(accountNumber);
    }
}