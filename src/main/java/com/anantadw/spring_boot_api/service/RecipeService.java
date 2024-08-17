package com.anantadw.spring_boot_api.service;

import java.util.List;

import com.anantadw.spring_boot_api.dto.ApiResponse;

public interface RecipeService {
    ApiResponse getRecipes(
            int userId,
            String recipeName,
            Integer levelId,
            Integer categoryId,
            Integer time,
            List<String> sortBy,
            int pageSize,
            int pageNumber);
}
