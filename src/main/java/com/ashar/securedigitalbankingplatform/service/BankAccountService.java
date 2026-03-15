package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.Transaction;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.repository.BankAccountRepository;
import com.ashar.securedigitalbankingplatform.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

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
        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }

    public BankAccount withdraw(String accountNumber, Double amount) {
        BankAccount account = bankAccountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not FOund"));
        if (amount <= 0) {
            throw new RuntimeException("Withdraw amount Must be positive");
        }
        if (account.getBalance() < amount) {
            throw new RuntimeException("insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setType("WITHDRAW");
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }

    @Transactional
    public void transfer(String fromAccount,
                         String toAccount,
                         Double amount) {
        if (fromAccount.equals(toAccount)) {
            throw new RuntimeException("cannot transfer to Same account");
        }
        BankAccount sender = bankAccountRepository
                .findByAccountNumber(fromAccount)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        BankAccount receiver = bankAccountRepository
                .findByAccountNumber(toAccount)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (amount <= 0) {
            throw new RuntimeException("enter positive amount");
        }
        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        
        Transaction senderTransaction = new Transaction();
        senderTransaction.setType("TRANSFER_OUT");
        senderTransaction.setAmount(amount);
        senderTransaction.setTimestamp(LocalDateTime.now());
        senderTransaction.setAccount(sender);

        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setType("TRANSFER_IN");
        receiverTransaction.setAmount(amount);
        receiverTransaction.setTimestamp(LocalDateTime.now());
        receiverTransaction.setAccount(receiver);

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);
        bankAccountRepository.save(sender);
        bankAccountRepository.save(receiver);
    }

    public List<Transaction> getAccountStatement(String accountNumber,
                                                 LocalDate startDate,
                                                 LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23,59,59);

        return transactionRepository
                .findByAccountAccountNumberAndTimestampBetween(accountNumber, start, end);
    }
}
