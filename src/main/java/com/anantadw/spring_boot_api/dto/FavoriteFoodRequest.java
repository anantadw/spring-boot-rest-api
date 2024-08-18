package com.anantadw.spring_boot_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FavoriteFoodRequest {
    @NotNull(message = "userId is required")
    @Positive(message = "userId must be a valid number")
    private int userId;
}
