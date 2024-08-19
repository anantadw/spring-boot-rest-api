package com.anantadw.spring_boot_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anantadw.spring_boot_api.dto.ApiResponse;
import com.anantadw.spring_boot_api.dto.LevelOptionDto;
import com.anantadw.spring_boot_api.entity.Level;
import com.anantadw.spring_boot_api.repository.LevelRepository;
import com.anantadw.spring_boot_api.service.LevelService;
import com.anantadw.spring_boot_api.util.ApiUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;

    @Override
    public ApiResponse getLevelOptionList() {
        List<Level> levels = levelRepository.findAll();
        List<LevelOptionDto> response = levels.stream()
                .map(this::mapToLevelOptionDto)
                .collect(Collectors.toList());

        return ApiUtil.buildApiResponse("Berhasil memuat Data Level",
                HttpStatus.OK,
                response,
                null,
                null);
    }

    private LevelOptionDto mapToLevelOptionDto(Level level) {
        LevelOptionDto response = new LevelOptionDto();
        response.setLevelId(level.getId());
        response.setLevelName(level.getName());

        return response;
    }
}
