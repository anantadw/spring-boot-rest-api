package com.anantadw.spring_boot_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.response.CategoryOptionResponse;
import com.anantadw.spring_boot_api.entity.Category;
import com.anantadw.spring_boot_api.repository.CategoryRepository;
import com.anantadw.spring_boot_api.service.CategoryService;
import com.anantadw.spring_boot_api.util.ApiUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse getCategoryOptionList() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryOptionResponse> responses = categories.stream()
                .map(this::mapToCategoryOptionResponse)
                .collect(Collectors.toList());

        return ApiUtil.buildApiResponse("Berhashil memuat Daftar Kategori",
                HttpStatus.OK,
                responses,
                null,
                null);
    }

    private CategoryOptionResponse mapToCategoryOptionResponse(Category category) {
        CategoryOptionResponse response = new CategoryOptionResponse();
        response.setCategoryId(category.getId());
        response.setCategoryName(category.getName());

        return response;
    }
}
