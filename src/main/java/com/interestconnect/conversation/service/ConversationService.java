package com.interestconnect.conversation.service;

import com.interestconnect.auth.service.AuthService;
import com.interestconnect.conversation.dto.ConversationResponse;
import com.interestconnect.conversation.entity.Conversation;
import com.interestconnect.conversation.repository.ConversationRepository;
import com.interestconnect.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AuthService authService;

    public ConversationService(
            ConversationRepository conversationRepository,
            AuthService authService) {

        this.conversationRepository = conversationRepository;
        this.authService = authService;
    }

    public List<ConversationResponse> getMyConversations() {

        User currentUser = authService.getCurrentUser();

        return conversationRepository
                .findByUser1OrUser2(currentUser, currentUser)
                .stream()
                .map(conversation -> new ConversationResponse(
                        conversation.getId(),
                        conversation.getConnectionRequest().getId(),
                        conversation.getUser1().getName(),
                        conversation.getUser2().getName()
                ))
                .toList();
    }
}