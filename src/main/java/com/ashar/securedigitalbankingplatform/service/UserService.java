package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.*;
import com.ashar.securedigitalbankingplatform.entity.Role;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.exception.InvalidCredentialsException;
import com.ashar.securedigitalbankingplatform.exception.UnauthorizedAccessException;
import com.ashar.securedigitalbankingplatform.exception.UserNotFoundException;
import com.ashar.securedigitalbankingplatform.repository.UserRepository;
import com.ashar.securedigitalbankingplatform.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LoginRateLimiterService loginRateLimiterService;

    public AuthResponseDTO register(UserRequestDTO request) {

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setCustomerId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setMessage("User registered successfully");

        return response;
    }

    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public User findUserEntityById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> {

                    UserResponseDTO dto =
                            new UserResponseDTO();

                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    return dto;
                })
                .toList();
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        String email = request.getEmail();
        loginRateLimiterService.checkBlocked(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    loginRateLimiterService.loginFailed(email);
                    return new InvalidCredentialsException(
                            "Invalid email or password"
                    );
                });

        boolean match = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!match) {
            loginRateLimiterService.loginFailed(email);

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        loginRateLimiterService.loginSuccess(email);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setMessage("Login successful");

        return response;
    }

    public User getLoggedInUser() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getName().equals("anonymousUser")) {

            throw new UnauthorizedAccessException("User not authenticated");
        }

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(user -> {

                    UserResponseDTO dto = new UserResponseDTO();

                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());

                    return dto;
                });
    }
}