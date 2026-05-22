package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.LoginRequestDTO;
import com.ashar.securedigitalbankingplatform.dto.LoginResponseDTO;
import com.ashar.securedigitalbankingplatform.entity.Role;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.exception.InvalidCredentialsException;
import com.ashar.securedigitalbankingplatform.repository.UserRepository;
import com.ashar.securedigitalbankingplatform.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldLoginSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"
        )).thenReturn(true);

        when(jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        )).thenReturn("fake-jwt-token");

        LoginResponseDTO response =
                userService.login(request);

        assertNotNull(response);

        assertEquals(
                "fake-jwt-token",
                response.getToken()
        );

        assertEquals(
                "Login successful",
                response.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionForInvalidPassword() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongpassword",
                "encodedPassword"
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );
    }
}