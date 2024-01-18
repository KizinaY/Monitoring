package com.kizina.monitoring.controller;

import com.kizina.monitoring.dto.ErrorResponse;
import com.kizina.monitoring.service.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .errorPhrase(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchUserException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .errorPhrase(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message("There is no user with id: " + exception.getUserId())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        UUID exceptionId = UUID.randomUUID();
        log.error("Exception with id: {}", exceptionId, exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorPhrase(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("Unknown exception with id: " + exceptionId)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
