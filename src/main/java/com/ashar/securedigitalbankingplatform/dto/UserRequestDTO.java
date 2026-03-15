package com.ashar.securedigitalbankingplatform.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
}