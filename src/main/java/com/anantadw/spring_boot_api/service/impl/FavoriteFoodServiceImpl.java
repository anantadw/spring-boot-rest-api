package com.anantadw.spring_boot_api.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.request.FavoriteFoodRequest;
import com.anantadw.spring_boot_api.entity.FavoriteFood;
import com.anantadw.spring_boot_api.entity.FavoriteFoodKey;
import com.anantadw.spring_boot_api.entity.Recipe;
import com.anantadw.spring_boot_api.entity.User;
import com.anantadw.spring_boot_api.repository.FavoriteFoodRepository;
import com.anantadw.spring_boot_api.repository.RecipeRepository;
import com.anantadw.spring_boot_api.repository.UserRepository;
import com.anantadw.spring_boot_api.service.FavoriteFoodService;
import com.anantadw.spring_boot_api.util.ApiUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteFoodServiceImpl implements FavoriteFoodService {
    private final FavoriteFoodRepository favoriteFoodRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ApiResponse toggleFavoriteRecipe(int recipeId, FavoriteFoodRequest request) {
        log.info("Toggle favorite recipe with recipeId {} and userId {}", recipeId, request.getUserId());
        FavoriteFood favoriteFood = favoriteFoodRepository
                .findByUserIdAndRecipeId(request.getUserId(), recipeId)
                .orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Resep dengan ID %d tidak ditemukan".formatted(recipeId)));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User dengan ID %d tidak ditemukan".formatted(request.getUserId())));
        String message = "";

        if (favoriteFood != null) {
            log.info("Favorite food found {}", favoriteFood);
            favoriteFood.setFavorite(!favoriteFood.isFavorite());

            message = favoriteFood.isFavorite()
                    ? "Resep \"%s\" berhasil ditambahkan ke favorit".formatted(recipe.getName())
                    : "Resep \"%s\" berhasil dihapus dari favorit".formatted(recipe.getName());
        } else {
            FavoriteFoodKey favoriteFoodKey = new FavoriteFoodKey(request.getUserId(), recipeId);
            favoriteFood = new FavoriteFood();
            favoriteFood.setId(favoriteFoodKey);
            favoriteFood.setUser(user);
            favoriteFood.setRecipe(recipe);

            message = "Resep \"%s\" berhasil ditambahkan ke favorit".formatted(recipe.getName());
        }

        favoriteFoodRepository.save(favoriteFood);

        return ApiUtil.buildApiResponse(
                message,
                HttpStatus.OK,
                null,
                null,
                (long) 1);
    }
}
