package com.ashar.securedigitalbankingplatform.service;

import com.ashar.securedigitalbankingplatform.dto.LoginRequestDTO;
import com.ashar.securedigitalbankingplatform.dto.LoginResponseDTO;
import com.ashar.securedigitalbankingplatform.dto.UserRequestDTO;
import com.ashar.securedigitalbankingplatform.entity.Role;
import com.ashar.securedigitalbankingplatform.entity.User;
import com.ashar.securedigitalbankingplatform.exception.InvalidCredentialsException;
import com.ashar.securedigitalbankingplatform.repository.UserRepository;
import com.ashar.securedigitalbankingplatform.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private LoginRateLimiterService loginRateLimiterService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUser() {

        UserRequestDTO request = new UserRequestDTO();

        request.setName("Ashar");
        request.setEmail("ashar@gmail.com");
        request.setPassword("123456");

        User savedUser = new User();

        savedUser.setId(1L);
        savedUser.setName("Ashar");
        savedUser.setEmail("ashar@gmail.com");
        savedUser.setRole(Role.USER);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        var response = userService.register(request);

        assertEquals("Ashar", response.getName());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("ashar@gmail.com");
        request.setPassword("123456");

        User user = new User();

        user.setEmail("ashar@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("ashar@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encodedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("fake-jwt-token");

        LoginResponseDTO response = userService.login(request);

        assertEquals("fake-jwt-token", response.getToken());

        verify(loginRateLimiterService)
                .checkBlocked("ashar@gmail.com");

        verify(loginRateLimiterService)
                .loginSuccess("ashar@gmail.com");
    }

    @Test
    void shouldThrowExceptionForInvalidPassword() {

        LoginRequestDTO request = new LoginRequestDTO();

        request.setEmail("ashar@gmail.com");
        request.setPassword("wrongpassword");

        User user = new User();

        user.setEmail("ashar@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("ashar@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongpassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );

        verify(loginRateLimiterService)
                .loginFailed("ashar@gmail.com");
    }
}