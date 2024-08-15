package com.anantadw.spring_boot_api.dto.auth;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String fullname;
    private String password;
    private String retypePassword;
}
