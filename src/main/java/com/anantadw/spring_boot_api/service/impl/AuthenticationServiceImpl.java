package com.anantadw.spring_boot_api.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.auth.SignInRequest;
import com.anantadw.spring_boot_api.dto.auth.SignInResponse;
import com.anantadw.spring_boot_api.dto.auth.SignUpRequest;
import com.anantadw.spring_boot_api.entity.User;
import com.anantadw.spring_boot_api.repository.UserRepository;
import com.anantadw.spring_boot_api.service.AuthenticationService;
import com.anantadw.spring_boot_api.service.JwtService;
import com.anantadw.spring_boot_api.util.ApiUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ApiResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username %s is already taken!".formatted(request.getUsername()));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullname(request.getFullname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return ApiUtil.buildApiResponse(
                "User %s registered successfully!".formatted(user.getUsername()),
                HttpStatus.CREATED,
                null,
                null,
                null);
    }

    @Override
    public ApiResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Username / password is wrong"));

            String token = jwtService.generateToken(user);

            SignInResponse response = new SignInResponse();
            response.setId(user.getId());
            response.setToken(token);
            response.setType("Bearer");
            response.setUsername(user.getUsername());
            response.setRole(user.getRole());

            return ApiUtil.buildApiResponse(
                    "User %s signed in successfully!".formatted(user.getUsername()),
                    HttpStatus.OK,
                    response,
                    null,
                    null);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
}
