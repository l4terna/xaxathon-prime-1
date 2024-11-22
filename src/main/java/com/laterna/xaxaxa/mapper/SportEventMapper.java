package com.laterna.xaxaxa.mapper;

import com.laterna.xaxaxa.dto.SportEventDto;
import com.laterna.xaxaxa.entity.SportEvent;
import com.laterna.xaxaxa.entity.SportCategory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SportEventMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public SportEvent toEntity(SportEventDto dto, SportCategory category) {
        if (dto == null) {
            return null;
        }

        SportEvent entity = SportEvent.builder()
                .eventId(dto.getId())
                .name(dto.getName())
                .location(dto.getLocation())
                .participants(dto.getParticipants())
                .description(dto.getDescription())
                .category(category)
                .build();

        if (dto.getDateStart() != null && !dto.getDateStart().isEmpty()) {
            entity.setDateStart(LocalDate.parse(dto.getDateStart(), DATE_FORMATTER));
        }

        if (dto.getDateEnd() != null && !dto.getDateEnd().isEmpty()) {
            entity.setDateEnd(LocalDate.parse(dto.getDateEnd(), DATE_FORMATTER));
        }

        return entity;
    }

    public SportEventDto toDto(SportEvent entity) {
        if (entity == null) {
            return null;
        }

        SportEventDto dto = SportEventDto.builder()
                .id(entity.getEventId())
                .name(entity.getName())
                .location(entity.getLocation())
                .participants(entity.getParticipants())
                .description(entity.getDescription())
                .build();

        // Преобразование дат
        if (entity.getDateStart() != null) {
            dto.setDateStart(entity.getDateStart().format(DATE_FORMATTER));
        }

        if (entity.getDateEnd() != null) {
            dto.setDateEnd(entity.getDateEnd().format(DATE_FORMATTER));
        }

        return dto;
    }

    public List<SportEvent> toEntityList(List<SportEventDto> dtoList, SportCategory category) {
        if (dtoList == null) {
            return null;
        }

        return dtoList.stream()
                .map(dto -> toEntity(dto, category))
                .collect(Collectors.toList());
    }

    public List<SportEventDto> toDtoList(List<SportEvent> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}