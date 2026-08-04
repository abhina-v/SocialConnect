package com.interestconnect.conversation.controller;

import com.interestconnect.conversation.dto.ConversationResponse;
import com.interestconnect.conversation.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Conversations",
        description = "Retrieve conversations between connected users"
)
@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(
            ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Operation(
            summary = "Get My Conversations",
            description = "Returns all conversations in which the authenticated user is a participant."
    )
    @GetMapping
    public List<ConversationResponse> getMyConversations() {

        return conversationService.getMyConversations();
    }
}