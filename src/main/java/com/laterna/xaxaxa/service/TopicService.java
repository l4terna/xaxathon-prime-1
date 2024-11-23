package com.laterna.xaxaxa.service;

import com.laterna.xaxaxa.dto.TopicCommentUpdateDto;
import com.laterna.xaxaxa.entity.Topic;
import com.laterna.xaxaxa.entity.TopicComment;
import com.laterna.xaxaxa.entity.User;
import com.laterna.xaxaxa.exception.ResourceNotFoundException;
import com.laterna.xaxaxa.repository.TopicRepository;
import com.laterna.xaxaxa.repository.TopicCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicCommentRepository commentRepository;
    private final UserService userService;

    /**
     * Создание нового топика.
     *
     * @param topic Сущность топика.
     * @return Сохраненный топик.
     */
    @Transactional
    public Topic createTopic(Topic topic) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new ResourceNotFoundException("Пользователь не найден или не аутентифицирован");
        }
        topic.setAuthor(currentUser);
        topic.setCreatedAt(LocalDateTime.now());
        return topicRepository.save(topic);
    }

    /**
     * Получение всех топиков с пагинацией и сортировкой.
     *
     * @param pageable Параметры пагинации и сортировки.
     * @return Страница топиков.
     */
    public Page<Topic> getAllTopics(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    /**
     * Получение топиков по идентификатору спортивного события с пагинацией и сортировкой.
     *
     * @param eventId  Идентификатор спортивного события.
     * @param pageable Параметры пагинации и сортировки.
     * @return Страница топиков.
     */
    public Page<Topic> getTopicsByEventId(Long eventId, Pageable pageable) {
        return topicRepository.findBySportEventId(eventId, pageable);
    }

    /**
     * Получение конкретного топика по его идентификатору.
     *
     * @param id Идентификатор топика.
     * @return Топик.
     */
    public Topic getTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Топик не найден с id: " + id));
    }

    // Методы для комментариев

    /**
     * Создание нового комментария к топику.
     *
     * @param topicId Идентификатор топика.
     * @param comment Сущность комментария.
     * @return Сохраненный комментарий.
     */
    @Transactional
    public TopicComment createComment(Long topicId, TopicComment comment) {
        Topic topic = getTopic(topicId);
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            throw new ResourceNotFoundException("Пользователь не найден или не аутентифицирован");
        }

        comment.setTopic(topic);
        comment.setAuthor(currentUser);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    /**
     * Получение всех комментариев к топику с пагинацией и сортировкой.
     *
     * @param topicId  Идентификатор топика.
     * @param pageable Параметры пагинации и сортировки.
     * @return Страница комментариев.
     */
    public Page<TopicComment> getCommentsByTopicId(Long topicId, Pageable pageable) {
        getTopic(topicId); // Проверка существования топика
        return commentRepository.findByTopicId(topicId, pageable);
    }

    /**
     * Получение конкретного комментария по его идентификатору и идентификатору топика.
     *
     * @param topicId   Идентификатор топика.
     * @param commentId Идентификатор комментария.
     * @return Комментарий.
     */
    public TopicComment getCommentByTopicId(Long topicId, Long commentId) {
        TopicComment comment = getComment(commentId);
        validateCommentBelongsToTopic(topicId, comment);
        return comment;
    }

    /**
     * Обновление комментария к топику.
     *
     * @param topicId   Идентификатор топика.
     * @param commentId Идентификатор комментария.
     * @param updateDto DTO для обновления комментария.
     * @return Обновленный комментарий.
     */
    @Transactional
    public TopicComment updateComment(Long topicId, Long commentId, TopicCommentUpdateDto updateDto) {
        TopicComment comment = getCommentByTopicId(topicId, commentId);
        validateCommentOwnership(comment);

        comment.setContent(updateDto.getContent());
        return commentRepository.save(comment);
    }

    /**
     * Удаление комментария из топики.
     *
     * @param topicId   Идентификатор топика.
     * @param commentId Идентификатор комментария.
     */
    @Transactional
    public void deleteComment(Long topicId, Long commentId) {
        TopicComment comment = getCommentByTopicId(topicId, commentId);
        validateCommentOwnership(comment);
        commentRepository.delete(comment);
    }

    // Вспомогательные методы

    /**
     * Получение комментария по его идентификатору.
     *
     * @param id Идентификатор комментария.
     * @return Комментарий.
     */
    private TopicComment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден с id: " + id));
    }

    /**
     * Проверка принадлежности комментария заданному топику.
     *
     * @param topicId Идентификатор топика.
     * @param comment Комментарий.
     */
    private void validateCommentBelongsToTopic(Long topicId, TopicComment comment) {
        if (!comment.getTopic().getId().equals(topicId)) {
            throw new ResourceNotFoundException("Комментарий не принадлежит указанному топику");
        }
    }

    /**
     * Проверка прав собственности на комментарий.
     *
     * @param comment Комментарий.
     */
    private void validateCommentOwnership(TopicComment comment) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null || !comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Вы можете изменять только свои комментарии");
        }
    }
}
