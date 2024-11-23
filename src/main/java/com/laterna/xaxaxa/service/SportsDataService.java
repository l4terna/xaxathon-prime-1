package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.dto.SportCategoryDto;
import com.laterna.xaxaxa.dto.SportEventDto;
import com.laterna.xaxaxa.entity.SportCategory;
import com.laterna.xaxaxa.entity.SportEvent;
import com.laterna.xaxaxa.mapper.SportCategoryMapper;
import com.laterna.xaxaxa.mapper.SportEventMapper;
import com.laterna.xaxaxa.repository.SportCategoryRepository;
import com.laterna.xaxaxa.repository.SportEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SportsDataService {
    private final SportsPDFParser sportsPDFParser;

    private final SportCategoryRepository categoryRepository;
    private final SportEventRepository sportEventRepository;

    private final SportCategoryMapper sportCategoryMapper;
    private final SportEventMapper sportEventMapper;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void saveData() {
        try {
            String url = "https://storage.minsport.gov.ru/cms-uploads/cms/II_chast_EKP_2024_14_11_24_65c6deea36.pdf";

            Map<SportCategoryDto, List<SportEventDto>> categoryEventsMap = sportsPDFParser.parse(url);

            for (Map.Entry<SportCategoryDto, List<SportEventDto>> entry : categoryEventsMap.entrySet()) {
                SportCategoryDto categoryDto = entry.getKey();
                List<SportEventDto> eventDtos = entry.getValue();
                log.info("Processing category: {}", categoryDto.getName());

                Optional<SportCategory> categoryOpt = categoryRepository.findByName(categoryDto.getName());
                SportCategory category = categoryOpt.orElseGet(() -> {
                    SportCategory newCategory = sportCategoryMapper.toEntity(categoryDto);
                    log.info("Saving new category: {}", newCategory);
                    return categoryRepository.save(newCategory);
                });

                List<SportEvent> events = sportEventMapper.toEntityList(eventDtos, category);
                List<SportEvent> eventsToSave = new ArrayList<>();

                for (SportEvent event : events) {
                    event.setCategory(category);

                    Optional<SportEvent> existingEventOpt = sportEventRepository.findByEventId((event.getEventId()));

                    boolean hasEventWithSameId = eventsToSave.stream()
                            .anyMatch(e -> e.getEventId().equals(event.getEventId()));

                    if (existingEventOpt.isEmpty() && !hasEventWithSameId) {
                        eventsToSave.add(event);
                    }
                }

                if (!eventsToSave.isEmpty()) {
                    log.info("Saving {} new events for category {}", eventsToSave.size(), category.getName());
                    sportEventRepository.saveAll(eventsToSave);
                }
            }
        } catch (Exception e) {
            log.error("Error while saving sports data", e);
        }
    }
}