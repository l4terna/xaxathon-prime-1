package com.laterna.xaxaxa.controller;

import com.laterna.xaxaxa.dto.*;
import com.laterna.xaxaxa.entity.Topic;
import com.laterna.xaxaxa.entity.TopicComment;
import com.laterna.xaxaxa.mapper.TopicMapper;
import com.laterna.xaxaxa.mapper.TopicCommentMapper;
import com.laterna.xaxaxa.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;
    private final TopicCommentMapper commentMapper;

    /**
     * Создание нового топика.
     *
     * @param topicDto DTO для создания топика.
     * @return DTO созданного топика.
     */
    @PostMapping
    public ResponseEntity<TopicResponseDto> createTopic(@Valid @RequestBody TopicCreateDto topicDto) {
        Topic topic = topicMapper.toEntity(topicDto);
        Topic savedTopic = topicService.createTopic(topic);
        return ResponseEntity.ok(topicMapper.toDto(savedTopic));
    }

    /**
     * Получение всех топиков с пагинацией и сортировкой по дате создания.
     *
     * @param pageable Параметры пагинации и сортировки.
     * @return Страница DTO топиков.
     */
    @GetMapping
    public ResponseEntity<Page<TopicResponseDto>> getAllTopics(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Topic> topicsPage = topicService.getAllTopics(pageable);
        Page<TopicResponseDto> dtoPage = topicsPage.map(topicMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Получение топиков по идентификатору спортивного события с пагинацией и сортировкой.
     *
     * @param eventId  Идентификатор спортивного события.
     * @param pageable Параметры пагинации и сортировки.
     * @return Страница DTO топиков.
     */
    @GetMapping("/sport-event/{eventId}")
    public ResponseEntity<Page<TopicResponseDto>> getTopicsByEvent(
            @PathVariable Long eventId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Topic> topicsPage = topicService.getTopicsByEventId(eventId, pageable);
        Page<TopicResponseDto> dtoPage = topicsPage.map(topicMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Получение конкретного топика по его идентификатору.
     *
     * @param id Идентификатор топика.
     * @return DTO топика.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDto> getTopic(@PathVariable Long id) {
        Topic topic = topicService.getTopic(id);
        return ResponseEntity.ok(topicMapper.toDto(topic));
    }

    // Endpoints для комментариев

    /**
     * Создание нового комментария к топику.
     *
     * @param topicId    Идентификатор топика.
     * @param commentDto DTO для создания комментария.
     * @return DTO созданного комментария.
     */
    @PostMapping("/{topicId}/comments")
    public ResponseEntity<TopicCommentResponseDto> createComment(
            @PathVariable Long topicId,
            @Valid @RequestBody TopicCommentCreateDto commentDto) {
        TopicComment comment = commentMapper.toEntity(commentDto);
        TopicComment savedComment = topicService.createComment(topicId, comment);
        return ResponseEntity.ok(commentMapper.toDto(savedComment));
    }

    /**
     * Получение всех комментариев к топику с пагинацией и сортировкой по дате создания.
     *
     * @param topicId  Идентификатор топика.
     * @param pageable Параметры пагинации и сортировки.
     * @return Страница DTO комментариев.
     */
    @GetMapping("/{topicId}/comments")
    public ResponseEntity<Page<TopicCommentResponseDto>> getTopicComments(
            @PathVariable Long topicId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TopicComment> commentsPage = topicService.getCommentsByTopicId(topicId, pageable);
        Page<TopicCommentResponseDto> dtoPage = commentsPage.map(commentMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Получение конкретного комментария по его идентификатору и идентификатору топика.
     *
     * @param topicId   Идентификатор топика.
     * @param commentId Идентификатор комментария.
     * @return DTO комментария.
     */
    @GetMapping("/{topicId}/comments/{commentId}")
    public ResponseEntity<TopicCommentResponseDto> getComment(
            @PathVariable Long topicId,
            @PathVariable Long commentId) {
        TopicComment comment = topicService.getCommentByTopicId(topicId, commentId);
        return ResponseEntity.ok(commentMapper.toDto(comment));
    }

    /**
     * Обновление комментария к топику.
     *
     * @param topicId   Идентификатор топика.
     * @param commentId Идентификатор комментария.
     * @param commentDto DTO для обновления комментария.
     * @return DTO обновленного комментария.
     */
    @PutMapping("/{topicId}/comments/{commentId}")
    public ResponseEntity<TopicCommentResponseDto> updateComment(
            @PathVariable Long topicId,
            @PathVariable Long commentId,
            @Valid @RequestBody TopicCommentUpdateDto commentDto) {
        TopicComment updatedComment = topicService.updateComment(topicId, commentId, commentDto);
        return ResponseEntity.ok(commentMapper.toDto(updatedComment));
    }

    /**
     * Удаление комментария из топика.
     *
     * @param topicId   Идентификатор топика.
     * @param commentId Идентификатор комментария.
     * @return Статус 204 No Content.
     */
    @DeleteMapping("/{topicId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long topicId,
            @PathVariable Long commentId) {
        topicService.deleteComment(topicId, commentId);
        return ResponseEntity.noContent().build();
    }
}
