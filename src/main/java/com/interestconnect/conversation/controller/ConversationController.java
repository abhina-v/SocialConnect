package com.interestconnect.conversation.controller;

import com.interestconnect.conversation.dto.ConversationResponse;
import com.interestconnect.conversation.service.ConversationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(
            ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    public List<ConversationResponse> getMyConversations() {
        return conversationService.getMyConversations();
    }
}