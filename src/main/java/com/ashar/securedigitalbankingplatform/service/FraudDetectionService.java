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

    public void checkLargeTransaction(String accountNumber, Double amount) {

        if (amount >= 10000) {

            FraudAlert alert = new FraudAlert();

            alert.setAccountNumber(accountNumber);
            alert.setAmount(amount);
            alert.setReason("LARGE TRANSACTION DETECTED");
            alert.setTimestamp(LocalDateTime.now());

            fraudAlertRepository.save(alert);
        }
    }
}