package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.SportCategoryDto;
import com.laterna.xaxaxa.entity.SportCategory;
import org.springframework.stereotype.Component;


@Component
public class SportCategoryMapper {

    public SportCategory toEntity(SportCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        return SportCategory.builder()
                .name(dto.getName())
                .build();
    }

    public SportCategoryDto toDto(SportCategory entity) {
        if (entity == null) {
            return null;
        }

        return SportCategoryDto.builder()
                .name(entity.getName())
                .build();
    }
}