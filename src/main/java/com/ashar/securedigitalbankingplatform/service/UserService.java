package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.UserRequestDTO;
import com.ashar.securedigitalbankingplatform.dto.UserResponseDTO;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO register(UserRequestDTO request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return response;
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
