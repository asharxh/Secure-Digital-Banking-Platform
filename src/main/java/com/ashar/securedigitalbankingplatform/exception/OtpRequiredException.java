package com.ashar.securedigitalbankingplatform.exception;

public class OtpRequiredException extends RuntimeException {

    public OtpRequiredException(String message) {
        super(message);
    }
}