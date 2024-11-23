package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.SportCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportCategoryRepository extends JpaRepository<SportCategory, Long> {
    Optional<SportCategory> findByName(String name);
    Page<SportCategory> findByNameContainingIgnoreCase(String query, Pageable pageable);
}
