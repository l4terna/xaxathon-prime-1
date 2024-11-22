package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.SportEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SportEventRepository extends JpaRepository<SportEvent, Long>, JpaSpecificationExecutor<SportEvent> {
    Optional<SportEvent> findByEventId(String eventId);
    Page<SportEvent> findByDateStartBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
