package com.interestconnect.message.controller;

import com.interestconnect.message.dto.MessageResponse;
import com.interestconnect.message.dto.SendMessageRequest;
import com.interestconnect.message.dto.UnreadCountResponse;
import com.interestconnect.message.service.MessageService;
import com.interestconnect.message.dto.UnreadCountResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public MessageResponse sendMessage(
            @Valid @RequestBody SendMessageRequest request) {

        return messageService.sendMessage(request);
    }

    @GetMapping("/{conversationId}")
    public List<MessageResponse> getConversationMessages(
            @PathVariable Long conversationId) {

        return messageService.getConversationMessages(conversationId);
    }

    @GetMapping("/{conversationId}/unread")
    public UnreadCountResponse getUnreadCount(
            @PathVariable Long conversationId) {

        return messageService.getUnreadCount(conversationId);
    }
}