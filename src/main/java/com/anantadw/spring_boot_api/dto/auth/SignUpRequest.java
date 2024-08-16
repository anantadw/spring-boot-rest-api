package com.anantadw.spring_boot_api.dto.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 1, max = 100, message = "Username must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "Username is not valid")
    private String username;

    @NotBlank(message = "Fullname cannot be empty")
    @Size(min = 1, max = 255, message = "Fullname must be between 1 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z.\s]*$", message = "Fullname is not valid")
    private String fullname;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;

    @NotBlank(message = "Retype password cannot be empty")
    private String retypePassword;

    @AssertTrue(message = "Passwords do not match")
    private boolean isRetypePassword() {
        return password != null && password.equals(retypePassword);
    }
}
