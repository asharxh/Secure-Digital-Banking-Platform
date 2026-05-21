package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.entity.FraudAlert;
import com.ashar.securedigitalbankingplatform.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudAlertRepository fraudAlertRepository;
    private final EmailService emailService;
    private final UserService userService;

    public void checkLargeTransaction(String accountNumber, Double amount) {

        if (amount >= 10000) {

            FraudAlert alert = new FraudAlert();

            alert.setAccountNumber(accountNumber);
            alert.setAmount(amount);
            alert.setReason("LARGE TRANSACTION DETECTED");
            alert.setTimestamp(LocalDateTime.now());

            fraudAlertRepository.save(alert);

            String email =
                    userService.getLoggedInUser()
                            .getEmail();

            emailService.sendEmail(
                    email,
                    "Suspicious Activity Alert",
                    "Large transaction detected on account "
                            + accountNumber
                            + " Amount: "
                            + amount
            );
        }
    }
}