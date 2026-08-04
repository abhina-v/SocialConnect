package com.interestconnect.websocket.controller;

import com.interestconnect.websocket.dto.ChatMessageRequest;
import com.interestconnect.websocket.dto.ChatMessageResponse;
import com.interestconnect.websocket.service.ChatWebSocketService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final ChatWebSocketService chatWebSocketService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(
            ChatWebSocketService chatWebSocketService,
            SimpMessagingTemplate messagingTemplate) {

        this.chatWebSocketService = chatWebSocketService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ChatMessageRequest request,
            Principal principal) {

        ChatMessageResponse response =
                chatWebSocketService.sendMessage(
                        request,
                        principal.getName()
                );

        messagingTemplate.convertAndSend(
                "/topic/conversations/" + request.getConversationId(),
                response
        );
    }
}