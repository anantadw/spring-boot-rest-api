package com.anantadw.spring_boot_api.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.anantadw.spring_boot_api.dto.CategoryOptionDto;
import com.anantadw.spring_boot_api.dto.LevelOptionDto;

import lombok.Data;

@Data
public class UpdateRecipeRequest {
    private int recipeId;
    private CategoryOptionDto categories;
    private LevelOptionDto levels;
    private String recipeName;
    private MultipartFile imageFilename;
    private int timeCook;
}
