package com.laterna.xaxaxa.repository;

import com.laterna.xaxaxa.entity.TopicComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicCommentRepository extends JpaRepository<TopicComment, Long> {
    Page<TopicComment> findByTopicId(Long topicId, Pageable pageable);
}
