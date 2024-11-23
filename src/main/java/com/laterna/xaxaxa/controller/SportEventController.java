package com.laterna.xaxaxa.controller;

import com.laterna.xaxaxa.entity.SportEvent;
import com.laterna.xaxaxa.service.SportEventService;
import com.laterna.xaxaxa.task.SportsPDFTask;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sport-events")
public class SportEventController {
    private final SportEventService sportEventService;
    private final SportsPDFTask sportsPDFTask;

    @GetMapping
    public ResponseEntity<Page<SportEvent>> getAllEvents(
            @Parameter(description = "Filter by months (1, 3, or 6)")
            @RequestParam(required = false) @Valid @Range(min = 1, max = 6) Integer months,
            @Parameter(description = "Filter by category name")
            @RequestParam(required = false) String category,
            @Parameter(description = "Filter by gender")
            @RequestParam(required = false) String gender,
            @Parameter(description = "Filter by country")
            @RequestParam(required = false) String country,
            @Parameter(description = "Filter by age")
            @RequestParam(required = false) String age,
            @Parameter(description = "Filter by location")
            @RequestParam(required = false) String location,
            @Parameter(description = "Filter by minimum participants")
            @RequestParam(required = false) Integer minParticipants,
            @Parameter(description = "Filter by maximum participants")
            @RequestParam(required = false) Integer maxParticipants,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Filter by end date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Search in description")
            @RequestParam(required = false) String description,
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Search by name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "30") int size) {

        return ResponseEntity.ok(sportEventService.getAllEvents(months, category, location,
                minParticipants, maxParticipants, startDate, endDate, description, country, gender, age, name, PageRequest.of(page, size)));
    }

    @PostMapping("/start")
    public ResponseEntity<Void> startProcess() {
        sportsPDFTask.runTask();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportEvent> getSportEventById(
            @PathVariable Long id) {
        SportEvent sportEvent = sportEventService.getEvent(id);
        return ResponseEntity.ok(sportEvent);
    }
}
