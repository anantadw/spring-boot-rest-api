package com.anantadw.spring_boot_api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.auth.SignInRequest;
import com.anantadw.spring_boot_api.dto.auth.SignUpRequest;
import com.anantadw.spring_boot_api.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-management/users")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        ApiResponse response = authenticationService.signUp(request);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping(path = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> signIn(@RequestBody @Valid SignInRequest request) {
        ApiResponse response = authenticationService.signIn(request);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
