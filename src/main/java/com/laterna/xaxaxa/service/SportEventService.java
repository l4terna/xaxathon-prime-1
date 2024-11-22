package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.entity.SportEvent;
import com.laterna.xaxaxa.repository.SportEventRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class SportEventService {
    private final SportEventRepository sportEventRepository;

    public Page<SportEvent> getAllEvents(Integer months, String category, String location,
                                         Integer minParticipants, Integer maxParticipants,
                                         LocalDate startDate, LocalDate endDate,
                                         String description, String country, String gender,
                                         String age,
                                         Pageable pageable) {

        Specification<SportEvent> spec = Specification.where(null);


        if (months != null) {
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusMonths(months);
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("dateStart"), currentDate, futureDate));
        }

        if (category != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("name"), category));
        }

        if (age != null) {
            spec = spec.and((root, query, cb) -> {
                String searchTerm = age + " лет";
                String searchTerm2 = age + " год";
                String searchTerm3 = age + " года";

                return cb.or(
                        cb.like(root.get("description"), "%" + searchTerm + "%"),
                        cb.like(root.get("description"), "%" + searchTerm2 + "%"),
                        cb.like(root.get("description"), "%" + searchTerm3 + "%")
                );
            });
        }

        if (location != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
        }

        if (minParticipants != null && maxParticipants != null) {
            spec = spec.and((root, query, cb) ->
                    cb.and(
                            cb.greaterThanOrEqualTo(root.get("participants"), minParticipants),
                            cb.lessThanOrEqualTo(root.get("participants"), maxParticipants)
                    ));
        } else {
            if (minParticipants != null) {
                spec = spec.and((root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get("participants"), minParticipants));
            }

            if (maxParticipants != null) {
                spec = spec.and((root, query, cb) ->
                        cb.lessThanOrEqualTo(root.get("participants"), maxParticipants));
            }
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("dateStart"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("dateEnd"), endDate));
        }

        if (description != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }

        if (country != null) {
            spec = spec.and((root, query, cb) -> {
                String searchTerm = " " + country.toLowerCase() + " ";
                return cb.like(
                        cb.lower(cb.concat(cb.concat(" ", root.get("description")), " ")),
                        "%" + searchTerm + "%"
                );
            });
        }

        if (gender != null) {
            List<String> maleKeywords = Arrays.asList("мужчины", "юноши", "юниоры");
            List<String> femaleKeywords = Arrays.asList("женщины", "девушки", "юниорки");

            List<String> keywords = gender.equalsIgnoreCase("MALE") ? maleKeywords :
                    gender.equalsIgnoreCase("FEMALE") ? femaleKeywords :
                            new ArrayList<>();

            if (!keywords.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    CriteriaBuilder.In<String> inClause = cb.in(cb.lower(root.get("description")));
                    keywords.forEach(keyword -> inClause.value("%" + keyword.toLowerCase() + "%"));
                    return inClause;
                });
            }
        }

        return sportEventRepository.findAll(spec, pageable);
    }
}
