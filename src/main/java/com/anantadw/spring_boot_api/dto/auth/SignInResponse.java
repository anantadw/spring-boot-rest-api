package com.anantadw.spring_boot_api.dto.auth;

import lombok.Data;

@Data
public class SignInResponse {
    private int id;
    private String token;
    private String type;
    private String username;
    private String role;
}
