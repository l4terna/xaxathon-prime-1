package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.dto.SportCategoryDto;
import com.laterna.xaxaxa.mapper.SportCategoryMapper;
import com.laterna.xaxaxa.repository.SportCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SportCategoryService {
    private final SportCategoryRepository categoryRepository;
    private final SportCategoryMapper sportCategoryMapper;

    public Page<SportCategoryDto> searchCategories(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return categoryRepository.findAll(pageable).map(sportCategoryMapper::toDto);
        }
        return categoryRepository.findByNameContainingIgnoreCase(query.trim(), pageable)
                .map(sportCategoryMapper::toDto);
    }
}