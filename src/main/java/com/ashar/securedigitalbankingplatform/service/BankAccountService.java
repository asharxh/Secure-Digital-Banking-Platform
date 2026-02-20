package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccount createAccount(User user, String accountNumber) {
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return bankAccountRepository.save(account);
    }

    public BankAccount deposit(String accountNumber, Double amount) {
        BankAccount account = bankAccountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (amount <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }
        account.setBalance(account.getBalance() + amount);
        return bankAccountRepository.save(account);
    }
}
