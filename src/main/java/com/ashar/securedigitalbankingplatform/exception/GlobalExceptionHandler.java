package com.ashar.securedigitalbankingplatform.exception;

import com.ashar.securedigitalbankingplatform.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO>
    handleUserNotFound(UserNotFoundException ex) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        ex.getMessage(),
                        404,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO>
    handleAccountNotFound(AccountNotFoundException ex) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        ex.getMessage(),
                        404,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponseDTO>
    handleInsufficientBalance(
            InsufficientBalanceException ex
    ) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        ex.getMessage(),
                        400,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponseDTO>
    handleUnauthorized(
            UnauthorizedAccessException ex
    ) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        ex.getMessage(),
                        403,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDTO>
    handleDuplicateEmail(
            DuplicateEmailException ex
    ) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        ex.getMessage(),
                        400,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO>
    handleRuntime(RuntimeException ex) {

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        ex.getMessage(),
                        400,
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(
            InvalidCredentialsException ex
    ) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getMessage(),
                401,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}