package com.interestconnect.message.controller;

import com.interestconnect.message.dto.MessageResponse;
import com.interestconnect.message.dto.SendMessageRequest;
import com.interestconnect.message.dto.UnreadCountResponse;
import com.interestconnect.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Messages",
        description = "Send and retrieve chat messages"
)
@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(
            summary = "Send Message",
            description = "Sends a message to an existing conversation."
    )
    @PostMapping("/send")
    public MessageResponse sendMessage(
            @Valid @RequestBody SendMessageRequest request) {

        return messageService.sendMessage(request);
    }

    @Operation(
            summary = "Get Conversation Messages",
            description = "Returns the complete chat history of a conversation and marks unread messages as read."
    )
    @GetMapping("/{conversationId}")
    public List<MessageResponse> getConversationMessages(
            @PathVariable Long conversationId) {

        return messageService.getConversationMessages(conversationId);
    }

    @Operation(
            summary = "Get Unread Message Count",
            description = "Returns the number of unread messages for the authenticated user in a conversation."
    )
    @GetMapping("/{conversationId}/unread")
    public UnreadCountResponse getUnreadCount(
            @PathVariable Long conversationId) {

        return messageService.getUnreadCount(conversationId);
    }
}