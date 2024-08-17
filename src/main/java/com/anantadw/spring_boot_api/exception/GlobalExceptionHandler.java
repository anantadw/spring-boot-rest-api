package com.anantadw.spring_boot_api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.util.ApiUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> apiException(ResponseStatusException e) {
        ApiResponse response = ApiUtil.buildApiResponse(
                e.getReason(),
                HttpStatus.valueOf(e.getStatusCode().value()),
                null,
                null,
                null);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> invalidArgumentException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(
                error.getField(),
                error.getDefaultMessage()));

        ApiResponse response = ApiUtil.buildApiResponse(
                "Validation Error",
                HttpStatus.BAD_REQUEST,
                null,
                errors,
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
