package com.ashar.securedigitalbankingplatform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LoginRateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "LOGIN_FAIL:";
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME_MINUTES = 5;

    public void checkBlocked(String email) {

        Object value = redisTemplate.opsForValue().get(PREFIX + email);

        if (value == null) return;

        int attempts = Integer.parseInt(value.toString());

        if (attempts >= MAX_ATTEMPTS) {
            throw new RuntimeException("Too many login attempts. Try again after 10 minutes.");
        }
    }

    public void loginFailed(String email) {

        String key = PREFIX + email;

        Object value = redisTemplate.opsForValue().get(key);

        int attempts = (value == null) ? 0 : Integer.parseInt(value.toString());

        attempts++;

        redisTemplate.opsForValue().set(
                key,
                attempts,
                Duration.ofMinutes(BLOCK_TIME_MINUTES)
        );
    }

    public void loginSuccess(String email) {
        redisTemplate.delete(PREFIX + email);
    }
}