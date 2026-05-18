package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.AccountCreateRequestDTO;
import com.ashar.securedigitalbankingplatform.dto.AccountResponseDTO;
import com.ashar.securedigitalbankingplatform.dto.TransactionResponseDTO;
import com.ashar.securedigitalbankingplatform.entity.AccountType;
import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.Transaction;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.exception.AccountNotFoundException;
import com.ashar.securedigitalbankingplatform.exception.InsufficientBalanceException;
import com.ashar.securedigitalbankingplatform.exception.UnauthorizedAccessException;
import com.ashar.securedigitalbankingplatform.repository.BankAccountRepository;
import com.ashar.securedigitalbankingplatform.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public AccountResponseDTO createAccount(
            User user,
            AccountCreateRequestDTO request
    ) {

        BankAccount account = new BankAccount();

        account.setUser(user);
        account.setBalance(0.0);
        account.setAccountNumber("ACC" + System.currentTimeMillis());

        account.setAccountType(request.getAccountType());

        BankAccount saved = bankAccountRepository.save(account);

        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountNumber(saved.getAccountNumber());
        dto.setBalance(saved.getBalance());

        return dto;
    }

    public BankAccount getUserAccount(String accountNumber) {

        User user = userService.getLoggedInUser();

        BankAccount account = bankAccountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Access denied: Not your account");
        }

        return account;
    }

    public BankAccount deposit(String accountNumber, Double amount) {

        BankAccount account = getUserAccount(accountNumber);

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        account.setBalance(account.getBalance() + amount);

        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setReferenceNumber("TXN" + System.currentTimeMillis());
        tx.setReceiverAccount(accountNumber);
        tx.setDescription("Cash deposit");
        tx.setAccount(account);

        transactionRepository.save(tx);

        return bankAccountRepository.save(account);
    }

    public BankAccount withdraw(String accountNumber, Double amount) {

        BankAccount account = getUserAccount(accountNumber);

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction tx = new Transaction();
        tx.setType("WITHDRAW");
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setReferenceNumber("TXN" + System.currentTimeMillis());
        tx.setSenderAccount(accountNumber);
        tx.setDescription("Cash withdrawal");
        tx.setAccount(account);

        transactionRepository.save(tx);

        return bankAccountRepository.save(account);
    }

    @Transactional
    public void transfer(String fromAccount, String toAccount, Double amount) {

        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        BankAccount sender = getUserAccount(fromAccount);

        BankAccount receiver = bankAccountRepository
                .findByAccountNumber(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("Receiver not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        String ref = "TXN" + System.currentTimeMillis();

        Transaction out = new Transaction();
        out.setType("TRANSFER_OUT");
        out.setAmount(amount);
        out.setTimestamp(LocalDateTime.now());
        out.setReferenceNumber(ref);
        out.setSenderAccount(fromAccount);
        out.setReceiverAccount(toAccount);
        out.setDescription("Money sent");
        out.setAccount(sender);

        Transaction in = new Transaction();
        in.setType("TRANSFER_IN");
        in.setAmount(amount);
        in.setTimestamp(LocalDateTime.now());
        in.setReferenceNumber(ref);
        in.setSenderAccount(fromAccount);
        in.setReceiverAccount(toAccount);
        in.setDescription("Money received");
        in.setAccount(receiver);

        transactionRepository.save(out);
        transactionRepository.save(in);

        bankAccountRepository.save(sender);
        bankAccountRepository.save(receiver);
    }

    public List<TransactionResponseDTO> getAccountStatement(
            String accountNumber,
            LocalDate startDate,
            LocalDate endDate
    ) {

        getUserAccount(accountNumber);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return transactionRepository
                .findByAccountAccountNumberAndTimestampBetween(accountNumber, start, end)
                .stream()
                .map(t -> {
                    TransactionResponseDTO dto = new TransactionResponseDTO();
                    dto.setReferenceNumber(t.getReferenceNumber());
                    dto.setType(t.getType());
                    dto.setAmount(t.getAmount());
                    dto.setSenderAccount(t.getSenderAccount());
                    dto.setReceiverAccount(t.getReceiverAccount());
                    dto.setDescription(t.getDescription());
                    dto.setTimestamp(t.getTimestamp());
                    return dto;
                })
                .toList();
    }

    public List<AccountResponseDTO> getMyAccounts() {

        User user = userService.getLoggedInUser();

        return bankAccountRepository.findByUserId(user.getId())
                .stream()
                .map(account -> {
                    AccountResponseDTO dto = new AccountResponseDTO();
                    dto.setAccountNumber(account.getAccountNumber());
                    dto.setBalance(account.getBalance());
                    return dto;
                })
                .toList();
    }
    public List<AccountResponseDTO> getAllAccountsForAdmin() {

        return bankAccountRepository.findAll()
                .stream()
                .map(account -> {

                    AccountResponseDTO dto = new AccountResponseDTO();

                    dto.setAccountNumber(account.getAccountNumber());
                    dto.setBalance(account.getBalance());

                    return dto;
                })
                .toList();
    }
    public Page<AccountResponseDTO> getAllAccountsForAdmin(Pageable pageable) {

        return bankAccountRepository.findAll(pageable)
                .map(account -> {

                    AccountResponseDTO dto = new AccountResponseDTO();

                    dto.setAccountNumber(account.getAccountNumber());
                    dto.setBalance(account.getBalance());

                    return dto;
                });
    }
}