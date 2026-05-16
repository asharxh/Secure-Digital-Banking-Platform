package com.ashar.securedigitalbankingplatform.exception;

public class UnauthorizedAccessException
        extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}