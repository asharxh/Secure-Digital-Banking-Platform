package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.TransactionResponseDTO;
import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.Transaction;
import com.ashar.securedigitalbankingplatform.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;

    public List<TransactionResponseDTO> getMyTransactions(
            String accountNumber
    ) {

        BankAccount account =
                bankAccountService.getUserAccount(accountNumber);

        List<Transaction> transactions =
                transactionRepository.findByAccountAccountNumber(
                        account.getAccountNumber()
                );

        return transactions.stream()
                .map(transaction -> {

                    TransactionResponseDTO dto =
                            new TransactionResponseDTO();

                    dto.setType(transaction.getType());

                    dto.setAmount(transaction.getAmount());

                    dto.setTimestamp(
                            transaction.getTimestamp()
                    );

                    return dto;
                })
                .toList();
    }

    public List<TransactionResponseDTO> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(tx -> {

                    TransactionResponseDTO dto = new TransactionResponseDTO();

                    dto.setReferenceNumber(tx.getReferenceNumber());
                    dto.setType(tx.getType());
                    dto.setAmount(tx.getAmount());
                    dto.setSenderAccount(tx.getSenderAccount());
                    dto.setReceiverAccount(tx.getReceiverAccount());
                    dto.setDescription(tx.getDescription());
                    dto.setTimestamp(tx.getTimestamp());

                    return dto;
                })
                .toList();
    }
}