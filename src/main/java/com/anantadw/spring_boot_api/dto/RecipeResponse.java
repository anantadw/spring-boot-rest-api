package com.anantadw.spring_boot_api.dto;

import lombok.Data;

@Data
public class RecipeResponse {
    @Data
    public static class Category {
        private int categoryId;
        private String categoryName;
    }

    @Data
    public static class Level {
        private int levelId;
        private String levelName;
    }

    private int recipeId;
    private Category categories;
    private Level levels;
    private String recipeName;
    private String imageUrl;
    private int time;
    private boolean isFavorite;
}