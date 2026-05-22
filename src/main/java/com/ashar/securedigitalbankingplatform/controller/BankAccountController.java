package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.dto.AccountCreateRequestDTO;
import com.ashar.securedigitalbankingplatform.dto.AccountResponseDTO;
import com.ashar.securedigitalbankingplatform.dto.TransactionResponseDTO;
import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.service.BankAccountService;
import com.ashar.securedigitalbankingplatform.service.OtpService;
import com.ashar.securedigitalbankingplatform.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final OtpService otpService;
    private final UserService userService;

    @PostMapping("/create")
    public AccountResponseDTO createAccount(
            @Valid @RequestBody AccountCreateRequestDTO request
    ) {
        User user = userService.getLoggedInUser();

        return bankAccountService.createAccount(user, request);
    }

    @PostMapping("/deposit")
    public BankAccount deposit(
            @RequestParam String accountNumber,
            @RequestParam @Positive(message = "Amount must be greater than 0") Double amount
    ) {
        return bankAccountService.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw")
    public BankAccount withdraw(
            @RequestParam String accountNumber,
            @RequestParam @Positive(message = "Amount must be greater than 0") Double amount
    ) {
        return bankAccountService.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam Double amount
    ) {

        bankAccountService.transfer(
                fromAccount,
                toAccount,
                amount
        );

        return "Transfer Successful";
    }
    @PostMapping("/transfer/verify")
    public String verifyTransfer(
            @RequestParam String email,
            @RequestParam String otp
    ) {

        boolean valid = otpService.verifyOtp(email, otp);

        if (!valid) {
            throw new RuntimeException("Invalid OTP");
        }

        var transfer =
                otpService.getPendingTransfer(email);

        if (transfer == null) {
            throw new RuntimeException(
                    "No pending transfer found"
            );
        }

        bankAccountService.completeVerifiedTransfer(
                transfer.getFromAccount(),
                transfer.getToAccount(),
                transfer.getAmount()
        );

        otpService.clearPendingTransfer(email);

        return "Transfer completed successfully";
    }

    @GetMapping("/statement")
    public List<TransactionResponseDTO> getStatement(

            @RequestParam String accountNumber,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return bankAccountService.getAccountStatement(
                accountNumber,
                startDate,
                endDate
        );
    }

    @GetMapping("/my")
    public List<AccountResponseDTO> getMyAccounts() {

        return bankAccountService.getMyAccounts();
    }
}