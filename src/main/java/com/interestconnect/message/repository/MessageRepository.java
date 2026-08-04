package com.interestconnect.message.repository;

import com.interestconnect.conversation.entity.Conversation;
import com.interestconnect.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationOrderByCreatedAtAsc(
            Conversation conversation
    );

    List<Message> findByConversationAndReadFalse(
            Conversation conversation
    );

    long countByConversationAndReadFalse(
            Conversation conversation
    );
}