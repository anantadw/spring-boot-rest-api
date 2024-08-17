package com.anantadw.spring_boot_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.FavoriteFoodRequest;
import com.anantadw.spring_boot_api.service.FavoriteFoodService;
import com.anantadw.spring_boot_api.service.RecipeService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/book-recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final FavoriteFoodService favoriteFoodService;

    @GetMapping(path = "/book-recipes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getRecipes(
            @RequestParam int userId,
            @RequestParam(required = false) String recipeName,
            @RequestParam(required = false) Integer levelId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer time,
            @RequestParam(defaultValue = "name,asc") List<String> sortBy,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber) {
        ApiResponse response = recipeService.getRecipes(userId, recipeName, levelId, categoryId, time, sortBy, pageSize,
                pageNumber);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping(path = "/book-recipes/{recipeId}/favorites", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> toggleFavoriteRecipe(@PathVariable int recipeId,
            @RequestBody FavoriteFoodRequest request) {
        ApiResponse response = favoriteFoodService.toggleFavoriteRecipe(recipeId, request);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
