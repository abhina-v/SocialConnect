package com.interestconnect.topic.service;

import com.interestconnect.auth.service.AuthService;
import com.interestconnect.common.exception.ForbiddenException;
import com.interestconnect.common.exception.ResourceNotFoundException;
import com.interestconnect.topic.dto.CreateTopicRequest;
import com.interestconnect.topic.dto.CreateTopicResponse;
import com.interestconnect.topic.dto.TopicResponse;
import com.interestconnect.topic.dto.UpdateTopicRequest;
import com.interestconnect.topic.entity.Topic;
import com.interestconnect.topic.entity.TopicStatus;
import com.interestconnect.topic.repository.TopicRepository;
import com.interestconnect.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final AuthService authService;

    public TopicService(TopicRepository topicRepository,
                        AuthService authService) {
        this.topicRepository = topicRepository;
        this.authService = authService;
    }
    @CacheEvict(value = "topics", key = "'all'")
    public CreateTopicResponse createTopic(CreateTopicRequest request) {

        User currentUser = authService.getCurrentUser();

        Topic topic = new Topic();

        topic.setTitle(request.getTitle());
        topic.setDescription(request.getDescription());
        topic.setTags(request.getTags());
        topic.setStatus(TopicStatus.LIVE);
        topic.setCreatedBy(currentUser);

        Topic savedTopic = topicRepository.save(topic);

        return new CreateTopicResponse(
                savedTopic.getId(),
                savedTopic.getTitle(),
                savedTopic.getDescription(),
                savedTopic.getTags(),
                savedTopic.getStatus(),
                savedTopic.getCreatedBy().getName()
        );
    }
    @Cacheable(value = "topics", key = "'all'")
    public List<TopicResponse> getAllTopics() {

        List<Topic> topics = topicRepository.findByStatus(TopicStatus.LIVE);

        return topics.stream()
                .map(topic -> new TopicResponse(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getDescription(),
                        topic.getTags(),
                        topic.getStatus(),
                        topic.getCreatedBy().getName()
                ))
                .toList();
    }

    public TopicResponse getTopicById(Long id) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found"));

        return new TopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getTags(),
                topic.getStatus(),
                topic.getCreatedBy().getName()
        );
    }

    public List<TopicResponse> searchTopics(String tag) {

        List<Topic> topics = topicRepository.findByTagsContainingIgnoreCase(tag);

        return topics.stream()
                .map(topic -> new TopicResponse(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getDescription(),
                        topic.getTags(),
                        topic.getStatus(),
                        topic.getCreatedBy().getName()
                ))
                .toList();
    }
    @CacheEvict(value = "topics", key = "'all'")
    public TopicResponse updateTopic(Long id, UpdateTopicRequest request) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found"));

        User currentUser = authService.getCurrentUser();

        if (!topic.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "You are not allowed to update this topic");
        }

        topic.setTitle(request.getTitle());
        topic.setDescription(request.getDescription());
        topic.setTags(request.getTags());

        Topic updatedTopic = topicRepository.save(topic);

        return new TopicResponse(
                updatedTopic.getId(),
                updatedTopic.getTitle(),
                updatedTopic.getDescription(),
                updatedTopic.getTags(),
                updatedTopic.getStatus(),
                updatedTopic.getCreatedBy().getName()
        );
    }
    @CacheEvict(value = "topics", key = "'all'")
    public TopicResponse closeTopic(Long id) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found"));

        User currentUser = authService.getCurrentUser();

        if (!topic.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "You are not allowed to close this topic");
        }

        topic.setStatus(TopicStatus.CLOSED);

        Topic updatedTopic = topicRepository.save(topic);

        return new TopicResponse(
                updatedTopic.getId(),
                updatedTopic.getTitle(),
                updatedTopic.getDescription(),
                updatedTopic.getTags(),
                updatedTopic.getStatus(),
                updatedTopic.getCreatedBy().getName()
        );
    }
    @CacheEvict(value = "topics", key = "'all'")
    public void deleteTopic(Long id) {

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found"));

        User currentUser = authService.getCurrentUser();

        if (!topic.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "You are not allowed to delete this topic");
        }

        topicRepository.delete(topic);
    }
}
