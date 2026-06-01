package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.entity.BankAccount;
import com.ashar.securedigitalbankingplatform.entity.Role;
import com.ashar.securedigitalbankingplatform.entity.TransactionCategory;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.exception.InsufficientBalanceException;
import com.ashar.securedigitalbankingplatform.repository.BankAccountRepository;
import com.ashar.securedigitalbankingplatform.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionCategorizationService categorizationService;

    @Mock
    private UserService userService;

    @Mock
    private AuditService auditService;

    @Mock
    private FraudDetectionService fraudDetectionService;

    @Mock
    private EmailService emailService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Test
    void shouldDepositSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setRole(Role.USER);

        BankAccount account = new BankAccount();
        account.setAccountNumber("ACC123");
        account.setBalance(1000.0);
        account.setUser(user);
        account.setFrozen(false);
        account.setStatus("ACTIVE");

        when(userService.getLoggedInUser())
                .thenReturn(user);

        when(bankAccountRepository.findByAccountNumber("ACC123"))
                .thenReturn(Optional.of(account));

        when(bankAccountRepository.save(any(BankAccount.class)))
                .thenReturn(account);

        when(categorizationService.categorize(anyString()))
                .thenReturn(TransactionCategory.TRANSFER);

        BankAccount result =
                bankAccountService.deposit("ACC123", 500.0);

        assertEquals(1500.0, result.getBalance());

        verify(bankAccountRepository, times(1))
                .save(account);

        verify(transactionRepository, times(1))
                .save(any());
    }

    @Test
    void shouldWithdrawSuccessfully() {

        User user = new User();
        user.setId(1L);

        BankAccount account = new BankAccount();
        account.setAccountNumber("ACC123");
        account.setBalance(5000.0);
        account.setUser(user);
        account.setFrozen(false);
        account.setStatus("ACTIVE");

        when(userService.getLoggedInUser())
                .thenReturn(user);

        when(bankAccountRepository.findByAccountNumber("ACC123"))
                .thenReturn(Optional.of(account));

        when(bankAccountRepository.save(any(BankAccount.class)))
                .thenReturn(account);

        when(categorizationService.categorize(anyString()))
                .thenReturn(TransactionCategory.TRANSFER);

        BankAccount result =
                bankAccountService.withdraw("ACC123", 1000.0);

        assertEquals(4000.0, result.getBalance());
    }

    @Test
    void shouldThrowInsufficientBalanceException() {

        User user = new User();
        user.setId(1L);

        BankAccount account = new BankAccount();
        account.setAccountNumber("ACC123");
        account.setBalance(500.0);
        account.setUser(user);
        account.setFrozen(false);
        account.setStatus("ACTIVE");

        when(userService.getLoggedInUser())
                .thenReturn(user);

        when(bankAccountRepository.findByAccountNumber("ACC123"))
                .thenReturn(Optional.of(account));

        assertThrows(
                InsufficientBalanceException.class,
                () -> bankAccountService.withdraw("ACC123", 1000.0)
        );
    }

    @Test
    void shouldTransferSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        BankAccount sender = new BankAccount();
        sender.setAccountNumber("ACC1");
        sender.setBalance(5000.0);
        sender.setUser(user);
        sender.setFrozen(false);
        sender.setStatus("ACTIVE");

        BankAccount receiver = new BankAccount();
        receiver.setAccountNumber("ACC2");
        receiver.setBalance(1000.0);
        receiver.setFrozen(false);
        receiver.setStatus("ACTIVE");

        when(userService.getLoggedInUser())
                .thenReturn(user);

        when(bankAccountRepository.findByAccountNumber("ACC1"))
                .thenReturn(Optional.of(sender));

        when(bankAccountRepository.findByAccountNumber("ACC2"))
                .thenReturn(Optional.of(receiver));

        when(categorizationService.categorize(anyString()))
                .thenReturn(TransactionCategory.TRANSFER);

        bankAccountService.transfer(
                "ACC1",
                "ACC2",
                2000.0
        );

        assertEquals(3000.0, sender.getBalance());
        assertEquals(3000.0, receiver.getBalance());

        verify(transactionRepository, times(2))
                .save(any());

        verify(bankAccountRepository, times(1))
                .save(sender);

        verify(bankAccountRepository, times(1))
                .save(receiver);
    }

    @Test
    void shouldThrowExceptionWhenTransferBalanceLow() {

        User user = new User();
        user.setId(1L);

        BankAccount sender = new BankAccount();
        sender.setAccountNumber("ACC1");
        sender.setBalance(500.0);
        sender.setUser(user);
        sender.setFrozen(false);
        sender.setStatus("ACTIVE");

        BankAccount receiver = new BankAccount();
        receiver.setAccountNumber("ACC2");
        receiver.setBalance(1000.0);
        receiver.setFrozen(false);
        receiver.setStatus("ACTIVE");

        when(userService.getLoggedInUser())
                .thenReturn(user);

        when(bankAccountRepository.findByAccountNumber("ACC1"))
                .thenReturn(Optional.of(sender));

        when(bankAccountRepository.findByAccountNumber("ACC2"))
                .thenReturn(Optional.of(receiver));

        assertThrows(
                InsufficientBalanceException.class,
                () -> bankAccountService.transfer(
                        "ACC1",
                        "ACC2",
                        2000.0
                )
        );
    }

    @Test
    void shouldTriggerOtpForLargeTransfer() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        BankAccount sender = new BankAccount();
        sender.setAccountNumber("ACC1");
        sender.setBalance(50000.0);
        sender.setUser(user);
        sender.setFrozen(false);
        sender.setStatus("ACTIVE");

        BankAccount receiver = new BankAccount();
        receiver.setAccountNumber("ACC2");
        receiver.setBalance(1000.0);
        receiver.setFrozen(false);
        receiver.setStatus("ACTIVE");

        when(userService.getLoggedInUser())
                .thenReturn(user);

        when(bankAccountRepository.findByAccountNumber("ACC1"))
                .thenReturn(Optional.of(sender));

        when(bankAccountRepository.findByAccountNumber("ACC2"))
                .thenReturn(Optional.of(receiver));

        when(otpService.generateOtp(user.getEmail()))
                .thenReturn("123456");

        assertThrows(
                RuntimeException.class,
                () -> bankAccountService.transfer(
                        "ACC1",
                        "ACC2",
                        15000.0
                )
        );

        verify(otpService, times(1))
                .generateOtp(user.getEmail());

        verify(emailService, times(1))
                .sendEmail(
                        eq(user.getEmail()),
                        anyString(),
                        anyString()
                );
    }
}