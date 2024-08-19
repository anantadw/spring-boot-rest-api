package com.anantadw.spring_boot_api.dto.response;

import com.anantadw.spring_boot_api.dto.CategoryOptionDto;
import com.anantadw.spring_boot_api.dto.LevelOptionDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RecipeResponse {
    private int recipeId;
    private CategoryOptionDto categories;
    private LevelOptionDto levels;
    private String recipeName;
    private String imageUrl;
    private int time;

    @JsonProperty("isFavorite")
    private boolean isFavorite;
}