package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.PendingTransferDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public String generateOtp(String email) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        redisTemplate.opsForValue().set(
                "OTP:" + email,
                otp,
                Duration.ofMinutes(5)
        );

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {

        Object storedOtp = redisTemplate.opsForValue().get("OTP:" + email);

        if (storedOtp == null) return false;

        boolean valid = storedOtp.toString().equals(otp);

        if (valid) {
            redisTemplate.delete("OTP:" + email);
        }

        return valid;
    }

    public void savePendingTransfer(String email, PendingTransferDTO transfer) {

        redisTemplate.opsForValue().set(
                "TRANSFER:" + email,
                transfer,
                Duration.ofMinutes(10)
        );
    }

    public PendingTransferDTO getPendingTransfer(String email) {

        Object data = redisTemplate.opsForValue().get("TRANSFER:" + email);

        if (data == null) return null;

        return objectMapper.convertValue(data, PendingTransferDTO.class);
    }

    public void clearPendingTransfer(String email) {

        redisTemplate.delete("TRANSFER:" + email);
        redisTemplate.delete("OTP:" + email);
    }
}