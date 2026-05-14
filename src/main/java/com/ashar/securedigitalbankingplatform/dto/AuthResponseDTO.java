package com.ashar.securedigitalbankingplatform.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {

    private Long customerId;
    private String name;
    private String email;
    private String message;
}