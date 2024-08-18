package com.anantadw.spring_boot_api.service;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.request.FavoriteFoodRequest;

public interface FavoriteFoodService {
    ApiResponse toggleFavoriteRecipe(int recipeId, FavoriteFoodRequest request);
}
