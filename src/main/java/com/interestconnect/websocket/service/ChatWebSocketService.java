package com.interestconnect.websocket.service;

import com.interestconnect.common.exception.ForbiddenException;
import com.interestconnect.common.exception.ResourceNotFoundException;
import com.interestconnect.conversation.entity.Conversation;
import com.interestconnect.conversation.repository.ConversationRepository;
import com.interestconnect.message.entity.Message;
import com.interestconnect.message.repository.MessageRepository;
import com.interestconnect.user.entity.User;
import com.interestconnect.user.repository.UserRepository;
import com.interestconnect.websocket.dto.ChatMessageRequest;
import com.interestconnect.websocket.dto.ChatMessageResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatWebSocketService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatWebSocketService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            UserRepository userRepository) {

        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public ChatMessageResponse sendMessage(
            ChatMessageRequest request,
            String email) {

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Conversation conversation = conversationRepository
                .findById(request.getConversationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conversation not found"));

        if (!conversation.getUser1().getId().equals(currentUser.getId())
                && !conversation.getUser2().getId().equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to send messages");
        }

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(request.getContent());

        Message saved = messageRepository.save(message);

        return new ChatMessageResponse(
                saved.getId(),
                conversation.getId(),
                currentUser.getName(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }
}
