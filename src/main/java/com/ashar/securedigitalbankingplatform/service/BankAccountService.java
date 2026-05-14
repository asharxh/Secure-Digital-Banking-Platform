package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.AccountResponseDTO;
import com.ashar.securedigitalbankingplatform.dto.TransactionResponseDTO;
import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.Transaction;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.repository.BankAccountRepository;
import com.ashar.securedigitalbankingplatform.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public AccountResponseDTO createAccount(User user) {

        BankAccount account = new BankAccount();

        account.setUser(user);

        account.setBalance(0.0);

        String accountNumber =
                "ACC" + System.currentTimeMillis();

        account.setAccountNumber(accountNumber);

        BankAccount savedAccount =
                bankAccountRepository.save(account);

        AccountResponseDTO response =
                new AccountResponseDTO();

        response.setAccountNumber(
                savedAccount.getAccountNumber()
        );

        response.setBalance(
                savedAccount.getBalance()
        );

        return response;
    }

    public BankAccount getUserAccount(String accountNumber) {

        User loggedInUser = userService.getLoggedInUser();

        System.out.println("Logged In User ID: "
                + loggedInUser.getId());

        System.out.println("Logged In User Email: "
                + loggedInUser.getEmail());

        BankAccount account = bankAccountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        System.out.println("Account Owner ID: "
                + account.getUser().getId());

        System.out.println("Requested Account: "
                + account.getAccountNumber());

        if (!account.getUser().getId()
                .equals(loggedInUser.getId())) {

            throw new RuntimeException(
                    "Access denied: Not your account"
            );
        }

        return account;
    }

    public BankAccount deposit(
            String accountNumber,
            Double amount
    ) {

        BankAccount account =
                getUserAccount(accountNumber);

        if (amount <= 0) {

            throw new RuntimeException(
                    "Deposit amount must be positive"
            );
        }

        account.setBalance(
                account.getBalance() + amount
        );

        Transaction transaction =
                new Transaction();

        transaction.setType("DEPOSIT");

        transaction.setAmount(amount);

        transaction.setTimestamp(
                LocalDateTime.now()
        );

        transaction.setReferenceNumber(
                "TXN" + System.currentTimeMillis()
        );

        transaction.setReceiverAccount(
                accountNumber
        );

        transaction.setDescription(
                "Cash deposit"
        );

        transaction.setAccount(account);

        transactionRepository.save(transaction);

        return bankAccountRepository.save(account);
    }

    public BankAccount withdraw(
            String accountNumber,
            Double amount
    ) {

        BankAccount account =
                getUserAccount(accountNumber);

        if (amount <= 0) {

            throw new RuntimeException(
                    "Withdraw amount must be positive"
            );
        }

        if (account.getBalance() < amount) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        account.setBalance(
                account.getBalance() - amount
        );

        Transaction transaction =
                new Transaction();

        transaction.setType("WITHDRAW");

        transaction.setAmount(amount);

        transaction.setTimestamp(
                LocalDateTime.now()
        );

        transaction.setReferenceNumber(
                "TXN" + System.currentTimeMillis()
        );

        transaction.setSenderAccount(
                accountNumber
        );

        transaction.setDescription(
                "Cash withdrawal"
        );

        transaction.setAccount(account);

        transactionRepository.save(transaction);

        return bankAccountRepository.save(account);
    }

    @Transactional
    public void transfer(
            String fromAccount,
            String toAccount,
            Double amount
    ) {

        if (fromAccount.equals(toAccount)) {

            throw new RuntimeException(
                    "Cannot transfer to same account"
            );
        }

        BankAccount sender =
                getUserAccount(fromAccount);

        BankAccount receiver =
                bankAccountRepository
                        .findByAccountNumber(toAccount)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Receiver not found"
                                ));

        if (amount <= 0) {

            throw new RuntimeException(
                    "Enter positive amount"
            );
        }

        if (sender.getBalance() < amount) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        sender.setBalance(
                sender.getBalance() - amount
        );

        receiver.setBalance(
                receiver.getBalance() + amount
        );

        String reference =
                "TXN" + System.currentTimeMillis();

        Transaction senderTransaction =
                new Transaction();

        senderTransaction.setType(
                "TRANSFER_OUT"
        );

        senderTransaction.setAmount(amount);

        senderTransaction.setTimestamp(
                LocalDateTime.now()
        );

        senderTransaction.setReferenceNumber(
                reference
        );

        senderTransaction.setSenderAccount(
                fromAccount
        );

        senderTransaction.setReceiverAccount(
                toAccount
        );

        senderTransaction.setDescription(
                "Money sent"
        );

        senderTransaction.setAccount(sender);

        Transaction receiverTransaction =
                new Transaction();

        receiverTransaction.setType(
                "TRANSFER_IN"
        );

        receiverTransaction.setAmount(amount);

        receiverTransaction.setTimestamp(
                LocalDateTime.now()
        );

        receiverTransaction.setReferenceNumber(
                reference
        );

        receiverTransaction.setSenderAccount(
                fromAccount
        );

        receiverTransaction.setReceiverAccount(
                toAccount
        );

        receiverTransaction.setDescription(
                "Money received"
        );

        receiverTransaction.setAccount(receiver);

        transactionRepository.save(
                senderTransaction
        );

        transactionRepository.save(
                receiverTransaction
        );

        bankAccountRepository.save(sender);

        bankAccountRepository.save(receiver);
    }

    public List<TransactionResponseDTO>
    getAccountStatement(
            String accountNumber,
            LocalDate startDate,
            LocalDate endDate
    ) {

        getUserAccount(accountNumber);

        LocalDateTime start =
                startDate.atStartOfDay();

        LocalDateTime end =
                endDate.atTime(23, 59, 59);

        List<Transaction> transactions =
                transactionRepository
                        .findByAccountAccountNumberAndTimestampBetween(
                                accountNumber,
                                start,
                                end
                        );

        return transactions.stream()
                .map(transaction -> {

                    TransactionResponseDTO dto =
                            new TransactionResponseDTO();

                    dto.setReferenceNumber(
                            transaction.getReferenceNumber()
                    );

                    dto.setType(
                            transaction.getType()
                    );

                    dto.setAmount(
                            transaction.getAmount()
                    );

                    dto.setSenderAccount(
                            transaction.getSenderAccount()
                    );

                    dto.setReceiverAccount(
                            transaction.getReceiverAccount()
                    );

                    dto.setDescription(
                            transaction.getDescription()
                    );

                    dto.setTimestamp(
                            transaction.getTimestamp()
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<AccountResponseDTO>
    getMyAccounts() {

        User loggedInUser =
                userService.getLoggedInUser();

        return bankAccountRepository
                .findByUserId(
                        loggedInUser.getId()
                )
                .stream()
                .map(account -> {

                    AccountResponseDTO dto =
                            new AccountResponseDTO();

                    dto.setAccountNumber(
                            account.getAccountNumber()
                    );

                    dto.setBalance(
                            account.getBalance()
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }
}