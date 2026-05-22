package com.ashar.securedigitalbankingplatform.controller;

import com.ashar.securedigitalbankingplatform.dto.LoginRequestDTO;
import com.ashar.securedigitalbankingplatform.dto.UserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String generateEmail() {
        return "testuser_" + UUID.randomUUID() + "@gmail.com";
    }

    @Test
    void shouldRegisterUser() throws Exception {

        UserRequestDTO request = new UserRequestDTO();
        request.setName("Test User");
        request.setEmail("testuser_" + System.currentTimeMillis() + "@gmail.com");
        request.setPassword("123456");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        String email = "login_" + System.currentTimeMillis() + "@gmail.com";

        UserRequestDTO register = new UserRequestDTO();
        register.setName("Login User");
        register.setEmail(email);
        register.setPassword("123456");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail(email);
        login.setPassword("123456");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }
}