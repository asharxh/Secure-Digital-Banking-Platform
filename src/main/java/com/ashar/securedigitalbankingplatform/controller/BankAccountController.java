package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.service.BankAccountService;
import com.ashar.securedigitalbankingplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor

public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserService userService;

    @PostMapping("/create")
    public BankAccount createAccount(@RequestParam Long userId,
                                     @RequestParam String accountNumber) {
        User user = userService.getUserById(userId); // we will create this method
        return bankAccountService.createAccount(user, accountNumber);
    }
}
