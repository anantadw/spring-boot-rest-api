package com.anantadw.spring_boot_api.service;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.auth.SignUpRequest;

public interface AuthenticationService {
    ApiResponse signUp(SignUpRequest request);
}
