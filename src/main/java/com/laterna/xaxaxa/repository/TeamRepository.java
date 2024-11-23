package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.Team;
import com.laterna.xaxaxa.entity.SportEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findBySportEvent(SportEvent sportEvent);
    boolean existsByName(String name);
}
