package com.anantadw.spring_boot_api.service;

import java.util.List;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.request.CreateRecipeRequest;
import com.anantadw.spring_boot_api.dto.request.UpdateRecipeRequest;

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

    ApiResponse getRecipeDetail(int recipeId);

    ApiResponse getUserRecipes();

    ApiResponse createRecipe(CreateRecipeRequest request);

    ApiResponse updateRecipe(UpdateRecipeRequest request);
}
