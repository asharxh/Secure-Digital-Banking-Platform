package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.dto.UserResponseDTO;
import com.ashar.securedigitalbankingplatform.dto.AccountResponseDTO;
import com.ashar.securedigitalbankingplatform.dto.TransactionResponseDTO;
import com.ashar.securedigitalbankingplatform.service.AuditService;
import com.ashar.securedigitalbankingplatform.service.UserService;
import com.ashar.securedigitalbankingplatform.service.BankAccountService;
import com.ashar.securedigitalbankingplatform.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ashar.securedigitalbankingplatform.entity.AuditLog;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final AuditService auditService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {
        return bankAccountService.getAllAccountsForAdmin(pageable);
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        return transactionService.getAllTransactions(pageable);
    }

    @GetMapping("/auditlogs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAuditLogs() {
        return auditService.getAllLogs();
    }
}