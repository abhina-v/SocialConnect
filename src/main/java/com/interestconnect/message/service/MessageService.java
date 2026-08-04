package com.interestconnect.message.service;

import com.interestconnect.auth.service.AuthService;
import com.interestconnect.common.exception.ForbiddenException;
import com.interestconnect.common.exception.ResourceNotFoundException;
import com.interestconnect.conversation.entity.Conversation;
import com.interestconnect.conversation.repository.ConversationRepository;
import com.interestconnect.message.dto.MessageResponse;
import com.interestconnect.message.dto.SendMessageRequest;
import com.interestconnect.message.dto.UnreadCountResponse;
import com.interestconnect.message.entity.Message;
import com.interestconnect.message.repository.MessageRepository;
import com.interestconnect.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AuthService authService;

    public MessageService(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            AuthService authService) {

        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.authService = authService;
    }

    public UnreadCountResponse getUnreadCount(Long conversationId) {

        User currentUser = authService.getCurrentUser();

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conversation not found"));

        if (!conversation.getUser1().getId().equals(currentUser.getId())
                && !conversation.getUser2().getId().equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to view this conversation");
        }

        long unread = messageRepository
                .findByConversationAndReadFalse(conversation)
                .stream()
                .filter(message ->
                        !message.getSender().getId().equals(currentUser.getId()))
                .count();

        return new UnreadCountResponse(unread);
    }

    public MessageResponse sendMessage(SendMessageRequest request) {

        User currentUser = authService.getCurrentUser();

        Conversation conversation = conversationRepository
                .findById(request.getConversationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conversation not found"));

        if (!conversation.getUser1().getId().equals(currentUser.getId())
                && !conversation.getUser2().getId().equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to send messages in this conversation");
        }

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(request.getContent());

        Message saved = messageRepository.save(message);

        return new MessageResponse(
                saved.getId(),
                saved.getConversation().getId(),
                saved.getSender().getName(),
                saved.getContent(),
                saved.isRead()
        );
    }

    public List<MessageResponse> getConversationMessages(Long conversationId) {

        User currentUser = authService.getCurrentUser();

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conversation not found"));

        if (!conversation.getUser1().getId().equals(currentUser.getId())
                && !conversation.getUser2().getId().equals(currentUser.getId())) {

            throw new ForbiddenException(
                    "You are not allowed to view this conversation");
        }

        List<Message> unreadMessages =
                messageRepository.findByConversationAndReadFalse(conversation);

        for (Message message : unreadMessages) {

            if (!message.getSender().getId().equals(currentUser.getId())) {
                message.setRead(true);
            }
        }

        messageRepository.saveAll(unreadMessages);

        return messageRepository
                .findByConversationOrderByCreatedAtAsc(conversation)
                .stream()
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getConversation().getId(),
                        message.getSender().getName(),
                        message.getContent(),
                        message.isRead()
                ))
                .toList();
    }
}