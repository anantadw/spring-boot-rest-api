package com.anantadw.spring_boot_api.service;

import java.util.List;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.request.FavoriteFoodRequest;

public interface FavoriteFoodService {
    ApiResponse toggleFavoriteRecipe(int recipeId, FavoriteFoodRequest request);

    ApiResponse getUserFavoriteRecipes(int userId,
            String recipeName,
            Integer levelId,
            Integer categoryId,
            Integer time,
            List<String> sortBy,
            int pageSize,
            int pageNumber);
}
