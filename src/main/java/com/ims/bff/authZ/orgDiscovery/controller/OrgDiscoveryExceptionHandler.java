package com.ims.bff.authZ.orgDiscovery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ims.bff.authZ.orgDiscovery.dto.OrgDiscoveryApiErrorResponse;
import com.ims.bff.authZ.orgDiscovery.exception.OrgDiscoveryException;

@RestControllerAdvice(basePackageClasses = OrgDiscoveryController.class)
public class OrgDiscoveryExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OrgDiscoveryApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().isEmpty()
                ? "Validation failed"
                : exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new OrgDiscoveryApiErrorResponse(message));
    }

    @ExceptionHandler(OrgDiscoveryException.class)
    public ResponseEntity<OrgDiscoveryApiErrorResponse> handleOrgDiscovery(OrgDiscoveryException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new OrgDiscoveryApiErrorResponse(exception.getMessage()));
    }
}
