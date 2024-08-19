package com.anantadw.spring_boot_api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.CategoryOptionDto;
import com.anantadw.spring_boot_api.dto.LevelOptionDto;
import com.anantadw.spring_boot_api.dto.request.CreateRecipeRequest;
import com.anantadw.spring_boot_api.dto.response.RecipeDetailResponse;
import com.anantadw.spring_boot_api.dto.response.RecipeResponse;
import com.anantadw.spring_boot_api.entity.Category;
import com.anantadw.spring_boot_api.entity.FavoriteFood;
import com.anantadw.spring_boot_api.entity.Level;
import com.anantadw.spring_boot_api.entity.Recipe;
import com.anantadw.spring_boot_api.entity.User;
import com.anantadw.spring_boot_api.repository.CategoryRepository;
import com.anantadw.spring_boot_api.repository.FavoriteFoodRepository;
import com.anantadw.spring_boot_api.repository.LevelRepository;
import com.anantadw.spring_boot_api.repository.RecipeRepository;
import com.anantadw.spring_boot_api.repository.UserRepository;
import com.anantadw.spring_boot_api.service.RecipeService;
import com.anantadw.spring_boot_api.util.ApiUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final FavoriteFoodRepository favoriteFoodRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LevelRepository levelRepository;

    @Override
    public ApiResponse getRecipes(
            int userId,
            String recipeName,
            Integer levelId,
            Integer categoryId,
            Integer time,
            List<String> sortBy,
            int pageSize,
            int pageNumber) {
        log.info(
                "userId: {}, recipeName: {}, levelId: {}, categoryId: {}, time: {}, sortBy: {}, pageSize: {}, pageNumber: {}",
                userId, recipeName, levelId, categoryId, time, sortBy, pageSize, pageNumber);
        Sort sort = Sort.by(Sort.Direction.fromString(sortBy.get(1)), sortBy.get(0));
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Specification<Recipe> spec = buildRecipeSpecification(recipeName, levelId, categoryId, time);

        Page<Recipe> recipes = recipeRepository.findAll(spec, pageRequest);
        log.info("Total elements: {}", recipes.getTotalElements());
        List<RecipeResponse> data = recipes
                .stream()
                .map(recipe -> mapToRecipeResponse(recipe, userId))
                .collect(Collectors.toList());

        return ApiUtil.buildApiResponse(
                "Berhasil memuat Resep Masakan",
                HttpStatus.OK,
                data,
                null,
                recipes.getTotalElements());
    }

    @Override
    public ApiResponse getRecipeDetail(int recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resep tidak ditemukan"));

        RecipeDetailResponse response = mapToRecipeDetailResponse(recipe);

        return ApiUtil.buildApiResponse(
                "Berhasil memuat Resep \"%s\"".formatted(recipe.getName()),
                HttpStatus.OK,
                response,
                null,
                (long) 1);
    }

    // ! Todo: Upload file using MinIO
    @Transactional
    @Override
    public ApiResponse createRecipe(CreateRecipeRequest request) {
        if (recipeRepository.existsByName(request.getRecipeName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Resep %s sudah ada!".formatted(request.getRecipeName()));
        }

        Recipe newRecipe = new Recipe();
        newRecipe.setName(request.getRecipeName());
        newRecipe.setTimeCook(request.getTimeCook());
        newRecipe.setIngredient(request.getIngredient());
        newRecipe.setHowToCook(request.getHowToCook());
        newRecipe.setImage(getImageName(request));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        newRecipe.setUser(user);

        Category category = categoryRepository.findById(request.getCategories().getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found"));
        newRecipe.setCategory(category);

        Level level = levelRepository.findById(request.getLevels().getLevelId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Level not found"));
        newRecipe.setLevel(level);

        recipeRepository.save(newRecipe);

        return ApiUtil.buildApiResponse("Recipe created successfully",
                HttpStatus.CREATED,
                null,
                null,
                null);
    }

    private Specification<Recipe> buildRecipeSpecification(
            String recipeName,
            Integer levelId,
            Integer categoryId,
            Integer time) {
        Specification<Recipe> spec = Specification.where(RecipeRepository.Specs.notDeleted());

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

    private RecipeResponse mapToRecipeResponse(Recipe recipe, int userId) {
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

        Optional<FavoriteFood> favoriteFood = favoriteFoodRepository.findByUserIdAndRecipeId(userId, recipe.getId());
        response.setFavorite(favoriteFood.map(FavoriteFood::isFavorite).orElse(false));

        return response;
    }

    // ! Todo: DRY
    private RecipeDetailResponse mapToRecipeDetailResponse(Recipe recipe) {
        RecipeDetailResponse response = new RecipeDetailResponse();
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
        response.setIngredient(recipe.getIngredient());
        response.setHowToCook(recipe.getHowToCook());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Optional<FavoriteFood> favoriteFood = favoriteFoodRepository.findByUserIdAndRecipeId(user.getId(),
                recipe.getId());
        response.setFavorite(favoriteFood.map(FavoriteFood::isFavorite).orElse(false));

        return response;
    }

    private String getImageName(CreateRecipeRequest request) {
        String imageName = "%s_%s_%s_%d.%s".formatted(
                request.getRecipeName().trim().toLowerCase().replaceAll("\\s", ""),
                request.getCategories().getCategoryName().trim().toLowerCase(),
                request.getLevels().getLevelName().trim().toLowerCase(),
                System.currentTimeMillis(),
                StringUtils.getFilenameExtension(request.getImageFilename().getOriginalFilename()));
        log.info("Image name: {}", imageName);

        return imageName;
    }
}
