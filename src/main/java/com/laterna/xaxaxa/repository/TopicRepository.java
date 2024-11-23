package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TopicRepository extends JpaRepository<Topic, Long> {
    Page<Topic> findBySportEventId(Long sportEventId, Pageable pageable);
}
