package com.anantadw.spring_boot_api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.service.LevelService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/book-recipe-masters")
@RequiredArgsConstructor
public class LevelController {
    private final LevelService levelService;

    @GetMapping(path = "/level-option-lists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getLevelOptionList() {
        ApiResponse response = levelService.getLevelOptionList();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
