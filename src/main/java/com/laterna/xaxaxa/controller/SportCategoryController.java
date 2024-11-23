package com.laterna.xaxaxa.controller;

import com.laterna.xaxaxa.dto.SportCategoryDto;
import com.laterna.xaxaxa.service.SportCategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sport-categories")
@RequiredArgsConstructor
public class SportCategoryController {
    private final SportCategoryService sportCategoryService;

    @GetMapping
    public ResponseEntity<Page<SportCategoryDto>> searchCategories(
            @Parameter(description = "Search query for category name")
            @RequestParam(required = false) String search,
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "30") int size) {

        return ResponseEntity.ok(sportCategoryService.searchCategories(search, PageRequest.of(page, size)));
    }

}
