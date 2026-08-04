package com.interestconnect.conversation.repository;

import com.interestconnect.conversation.entity.Conversation;
import com.interestconnect.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUser1OrUser2(User user1, User user2);

    Optional<Conversation> findByConnectionRequestId(Long connectionRequestId);
}