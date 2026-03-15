package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.Transaction;
import com.ashar.securedigitalbankingplatform.repository.TransactionRepository;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.service.BankAccountService;
import com.ashar.securedigitalbankingplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor

public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserService userService;
    private final TransactionRepository transactionRepository;

    @PostMapping("/create")
    public BankAccount createAccount(@RequestParam Long userId,
                                     @RequestParam String accountNumber) {
        User user = userService.findUserEntityById(userId); // getUser by ID method is created in UserService
        // If User Not Available then it'll show runtime exception " user is not found"
        return bankAccountService.createAccount(user, accountNumber);
    }

    @PostMapping("/deposit")
    public BankAccount deposit (@RequestParam String accountNumber,
                                @RequestParam Double amount){
        return bankAccountService.deposit(accountNumber,amount);
    }

    @PostMapping("/withdraw")
    public BankAccount withdraw(@RequestParam String accountNumber,
                                @RequestParam Double amount) {
        return bankAccountService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount,
                           @RequestParam String toAccount,
                           @RequestParam Double amount) {
        bankAccountService.transfer(fromAccount, toAccount, amount);
        return "Transfer Successful";
    }
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam String accountNumber) {
        return transactionRepository.findByAccountAccountNumber(accountNumber);
    }

    @GetMapping("/statement")
    public List<Transaction> getStatement(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return bankAccountService.getAccountStatement(accountNumber, start, end);
    }
}
