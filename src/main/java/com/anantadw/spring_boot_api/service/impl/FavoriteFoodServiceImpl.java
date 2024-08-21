package com.anantadw.spring_boot_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.CategoryOptionDto;
import com.anantadw.spring_boot_api.dto.LevelOptionDto;
import com.anantadw.spring_boot_api.dto.request.FavoriteFoodRequest;
import com.anantadw.spring_boot_api.dto.response.RecipeResponse;
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

    @Override
    public ApiResponse getUserFavoriteRecipes(
            int userId,
            String recipeName,
            Integer levelId,
            Integer categoryId,
            Integer time,
            List<String> sortBy,
            int pageSize,
            int pageNumber) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortBy.get(1)), sortBy.get(0));
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Specification<Recipe> spec = buildRecipeSpecification(recipeName, levelId, categoryId, time, userId);

        Page<Recipe> recipes = recipeRepository.findAll(spec, pageRequest);
        List<RecipeResponse> data = recipes
                .stream()
                .map(this::mapToRecipeResponse)
                .collect(Collectors.toList());

        return ApiUtil.buildApiResponse(
                "Berhasil memuat Resep Masakan Favorit",
                HttpStatus.OK,
                data,
                null,
                (long) data.size());
    }

    // ! Todo: DRY
    private Specification<Recipe> buildRecipeSpecification(
            String recipeName,
            Integer levelId,
            Integer categoryId,
            Integer time,
            Integer userId) {
        Specification<Recipe> spec = Specification.where(RecipeRepository.Specs.notDeleted())
                .and(RecipeRepository.Specs.hasFavoriteByUser(userId));

        if (recipeName != null && !recipeName.trim().isEmpty()) {
            spec = spec.and(RecipeRepository.Specs.recipeNameContains(recipeName));
        }

        if (levelId != null) {
            spec = spec.and(RecipeRepository.Specs.levelIdEquals(levelId));
        }

        if (categoryId != null) {
            spec = spec.and(RecipeRepository.Specs.categoryIdEquals(categoryId));
        }

        if (time != null) {
            spec = spec.and(RecipeRepository.Specs.timeEquals(time));
        }

        return spec;
    }

    // ! Todo: DRY
    private RecipeResponse mapToRecipeResponse(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        CategoryOptionDto category = new CategoryOptionDto();
        LevelOptionDto level = new LevelOptionDto();

        response.setRecipeId(recipe.getId());

        category.setCategoryId(recipe.getCategory().getId());
        category.setCategoryName(recipe.getCategory().getName());
        response.setCategories(category);

        level.setLevelId(recipe.getLevel().getId());
        level.setLevelName(recipe.getLevel().getName());
        response.setLevels(level);

        response.setRecipeName(recipe.getName());
        response.setImageUrl(recipe.getImage());
        response.setTime(recipe.getTimeCook());
        response.setFavorite(true);

        return response;
    }
}
