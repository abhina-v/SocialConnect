package com.interestconnect.connection.repository;

import com.interestconnect.connection.entity.ConnectionRequest;
import com.interestconnect.connection.entity.ConnectionStatus;
import com.interestconnect.topic.entity.Topic;
import com.interestconnect.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository
        extends JpaRepository<ConnectionRequest, Long> {

    List<ConnectionRequest> findByReceiver(User receiver);

    List<ConnectionRequest> findBySender(User sender);

    List<ConnectionRequest> findByReceiverAndStatus(
            User receiver,
            ConnectionStatus status
    );

    List<ConnectionRequest> findBySenderAndStatus(
            User sender,
            ConnectionStatus status
    );

    Optional<ConnectionRequest> findBySenderAndReceiverAndTopic(
            User sender,
            User receiver,
            Topic topic
    );

    Optional<ConnectionRequest> findByIdAndReceiver(
            Long id,
            User receiver
    );
}