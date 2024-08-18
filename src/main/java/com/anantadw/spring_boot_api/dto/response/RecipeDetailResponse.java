package com.anantadw.spring_boot_api.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeDetailResponse extends RecipeResponse {
    private String ingredient;
    private String howToCook;
}
