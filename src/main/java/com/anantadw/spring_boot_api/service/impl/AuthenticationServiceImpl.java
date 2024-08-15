package com.anantadw.spring_boot_api.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.auth.SignUpRequest;
import com.anantadw.spring_boot_api.model.User;
import com.anantadw.spring_boot_api.repository.UserRepository;
import com.anantadw.spring_boot_api.service.AuthenticationService;
import com.anantadw.spring_boot_api.util.ApiUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiUtil.buildApiResponse(
                    "Username %s is already taken!".formatted(request.getUsername()),
                    HttpStatus.CONFLICT,
                    null);
        }

        if (!request.getPassword().equals(request.getRetypePassword())) {
            return ApiUtil.buildApiResponse(
                    "Password and retype password do not match!",
                    HttpStatus.CONFLICT,
                    null);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullname(request.getFullname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ApiUtil.buildApiResponse(
                "User %s registered successfully!".formatted(user.getUsername()),
                HttpStatus.CREATED,
                null);
    }
}
