package com.interestconnect.topic.controller;

import com.interestconnect.topic.dto.CreateTopicRequest;
import com.interestconnect.topic.dto.CreateTopicResponse;
import com.interestconnect.topic.dto.TopicResponse;
import com.interestconnect.topic.dto.UpdateTopicRequest;
import com.interestconnect.topic.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Topics",
        description = "Create and manage discussion topics"
)
@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(
            summary = "Create Topic",
            description = "Creates a new discussion topic for the authenticated user."
    )
    @PostMapping
    public CreateTopicResponse createTopic(
            @Valid @RequestBody CreateTopicRequest request) {

        return topicService.createTopic(request);
    }

    @Operation(
            summary = "Get All Topics",
            description = "Returns all currently active (LIVE) topics."
    )
    @GetMapping
    public List<TopicResponse> getAllTopics() {

        return topicService.getAllTopics();
    }

    @Operation(
            summary = "Get Topic By ID",
            description = "Returns the details of a topic using its unique ID."
    )
    @GetMapping("/{id}")
    public TopicResponse getTopicById(@PathVariable Long id) {

        return topicService.getTopicById(id);
    }

    @Operation(
            summary = "Search Topics",
            description = "Searches topics by matching the provided tag."
    )
    @GetMapping("/search")
    public List<TopicResponse> searchTopics(
            @RequestParam String tag) {

        return topicService.searchTopics(tag);
    }

    @Operation(
            summary = "Update Topic",
            description = "Updates an existing topic. Only the topic owner can perform this operation."
    )
    @PutMapping("/{id}")
    public TopicResponse updateTopic(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTopicRequest request) {

        return topicService.updateTopic(id, request);
    }

    @Operation(
            summary = "Close Topic",
            description = "Marks a topic as CLOSED. Only the topic owner can close it."
    )
    @PatchMapping("/{id}/close")
    public TopicResponse closeTopic(@PathVariable Long id) {

        return topicService.closeTopic(id);
    }

    @Operation(
            summary = "Delete Topic",
            description = "Deletes a topic permanently. Only the topic owner can delete it."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {

        topicService.deleteTopic(id);

        return ResponseEntity.noContent().build();
    }
}