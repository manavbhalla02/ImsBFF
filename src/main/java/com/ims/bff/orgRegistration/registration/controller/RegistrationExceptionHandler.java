package com.ims.bff.orgRegistration.registration.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ims.bff.orgRegistration.registration.dto.ApiErrorResponse;
import com.ims.bff.orgRegistration.registration.exception.DuplicateDomainException;

@RestControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(DuplicateDomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateDomain(DuplicateDomainException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ApiErrorResponse(message));
    }
}
