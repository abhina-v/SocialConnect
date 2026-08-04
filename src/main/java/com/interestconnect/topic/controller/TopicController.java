package com.interestconnect.topic.controller;
import java.util.List;

import com.interestconnect.topic.dto.CreateTopicRequest;
import com.interestconnect.topic.dto.UpdateTopicRequest;
import com.interestconnect.topic.dto.CreateTopicResponse;
import com.interestconnect.topic.dto.TopicResponse;
import com.interestconnect.topic.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public CreateTopicResponse createTopic(
            @Valid @RequestBody CreateTopicRequest request) {

        return topicService.createTopic(request);
    }
    @GetMapping
    public List<TopicResponse> getAllTopics() {

        return topicService.getAllTopics();

    }
    @GetMapping("/{id}")
    public TopicResponse getTopicById(@PathVariable Long id) {

        return topicService.getTopicById(id);

    }
    @GetMapping("/search")
    public List<TopicResponse> searchTopics(
            @RequestParam String tag) {

        return topicService.searchTopics(tag);
    }
    @PutMapping("/{id}")
    public TopicResponse updateTopic(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTopicRequest request) {

        return topicService.updateTopic(id, request);
    }
    @PatchMapping("/{id}/close")
    public TopicResponse closeTopic(@PathVariable Long id) {

        return topicService.closeTopic(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {

        topicService.deleteTopic(id);

        return ResponseEntity.noContent().build();
    }
}