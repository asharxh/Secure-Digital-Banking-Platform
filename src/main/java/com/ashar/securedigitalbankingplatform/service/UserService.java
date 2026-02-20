package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    public User register(User user) {
        // Optional: check if email already exists
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        return userRepository.save(user);
    }

}
