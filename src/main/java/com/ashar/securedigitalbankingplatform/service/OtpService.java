package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.PendingTransferDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStore = new HashMap<>();

    private final Map<String, PendingTransferDTO>
            pendingTransfers = new HashMap<>();

    public String generateOtp(String email) {

        String otp = String.valueOf(
                new Random().nextInt(900000) + 100000
        );

        otpStore.put(email, otp);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {

        return otp.equals(otpStore.get(email));
    }

    public void savePendingTransfer(
            String email,
            PendingTransferDTO transfer
    ) {

        pendingTransfers.put(email, transfer);
    }

    public PendingTransferDTO getPendingTransfer(
            String email
    ) {

        return pendingTransfers.get(email);
    }

    public void clearPendingTransfer(String email) {

        pendingTransfers.remove(email);
        otpStore.remove(email);
    }
}