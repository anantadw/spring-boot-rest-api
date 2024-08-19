package com.anantadw.spring_boot_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RecipeResponse {
    private int recipeId;
    private CategoryOptionResponse categories;
    private LevelOptionResponse levels;
    private String recipeName;
    private String imageUrl;
    private int time;

    @JsonProperty("isFavorite")
    private boolean isFavorite;
}