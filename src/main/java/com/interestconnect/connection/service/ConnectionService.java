package com.interestconnect.connection.service;

import com.interestconnect.auth.service.AuthService;
import com.interestconnect.common.exception.BadRequestException;
import com.interestconnect.common.exception.ResourceNotFoundException;
import com.interestconnect.connection.dto.ConnectionResponse;
import com.interestconnect.connection.dto.SendConnectionRequest;
import com.interestconnect.connection.entity.ConnectionRequest;
import com.interestconnect.connection.entity.ConnectionStatus;
import com.interestconnect.connection.repository.ConnectionRequestRepository;
import com.interestconnect.conversation.entity.Conversation;
import com.interestconnect.conversation.repository.ConversationRepository;
import com.interestconnect.topic.entity.Topic;
import com.interestconnect.topic.repository.TopicRepository;
import com.interestconnect.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionService {

    private final ConnectionRequestRepository connectionRequestRepository;
    private final TopicRepository topicRepository;
    private final AuthService authService;
    private final ConversationRepository conversationRepository;

    public ConnectionService(
            ConnectionRequestRepository connectionRequestRepository,
            TopicRepository topicRepository,
            AuthService authService,
            ConversationRepository conversationRepository) {

        this.connectionRequestRepository = connectionRequestRepository;
        this.topicRepository = topicRepository;
        this.authService = authService;
        this.conversationRepository = conversationRepository;
    }

    public List<ConnectionResponse> getReceivedRequests() {

        User currentUser = authService.getCurrentUser();

        return connectionRequestRepository
                .findByReceiver(currentUser)
                .stream()
                .map(connection -> new ConnectionResponse(
                        connection.getId(),
                        connection.getSender().getName(),
                        connection.getReceiver().getName(),
                        connection.getTopic().getTitle(),
                        connection.getStatus()
                ))
                .toList();
    }

    public List<ConnectionResponse> getSentRequests() {

        User currentUser = authService.getCurrentUser();

        return connectionRequestRepository
                .findBySender(currentUser)
                .stream()
                .map(connection -> new ConnectionResponse(
                        connection.getId(),
                        connection.getSender().getName(),
                        connection.getReceiver().getName(),
                        connection.getTopic().getTitle(),
                        connection.getStatus()
                ))
                .toList();
    }

    public ConnectionResponse acceptRequest(Long requestId) {

        User currentUser = authService.getCurrentUser();

        ConnectionRequest connectionRequest =
                connectionRequestRepository
                        .findByIdAndReceiver(requestId, currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Connection request not found"));

        if (connectionRequest.getStatus() != ConnectionStatus.PENDING) {
            throw new BadRequestException("Request already processed");
        }

        connectionRequest.setStatus(ConnectionStatus.ACCEPTED);

        ConnectionRequest updated =
                connectionRequestRepository.save(connectionRequest);

        // Create conversation automatically
        Conversation conversation = new Conversation();

        conversation.setConnectionRequest(updated);
        conversation.setUser1(updated.getSender());
        conversation.setUser2(updated.getReceiver());

        conversationRepository.save(conversation);

        return new ConnectionResponse(
                updated.getId(),
                updated.getSender().getName(),
                updated.getReceiver().getName(),
                updated.getTopic().getTitle(),
                updated.getStatus()
        );
    }

    public ConnectionResponse rejectRequest(Long requestId) {

        User currentUser = authService.getCurrentUser();

        ConnectionRequest connectionRequest =
                connectionRequestRepository
                        .findByIdAndReceiver(requestId, currentUser)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Connection request not found"));

        if (connectionRequest.getStatus() != ConnectionStatus.PENDING) {
            throw new BadRequestException("Request already processed");
        }

        connectionRequest.setStatus(ConnectionStatus.REJECTED);

        ConnectionRequest updated =
                connectionRequestRepository.save(connectionRequest);

        return new ConnectionResponse(
                updated.getId(),
                updated.getSender().getName(),
                updated.getReceiver().getName(),
                updated.getTopic().getTitle(),
                updated.getStatus()
        );
    }

    public ConnectionResponse sendConnectionRequest(
            SendConnectionRequest request) {

        User sender = authService.getCurrentUser();

        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Topic not found"));

        User receiver = topic.getCreatedBy();

        // User cannot connect to their own topic
        if (sender.getId().equals(receiver.getId())) {
            throw new BadRequestException(
                    "You cannot connect to your own topic");
        }

        // Prevent duplicate request
        connectionRequestRepository
                .findBySenderAndReceiverAndTopic(sender, receiver, topic)
                .ifPresent(connection -> {
                    throw new BadRequestException(
                            "Connection request already exists");
                });

        ConnectionRequest connectionRequest = new ConnectionRequest();

        connectionRequest.setSender(sender);
        connectionRequest.setReceiver(receiver);
        connectionRequest.setTopic(topic);
        connectionRequest.setStatus(ConnectionStatus.PENDING);

        ConnectionRequest saved =
                connectionRequestRepository.save(connectionRequest);

        return new ConnectionResponse(
                saved.getId(),
                saved.getSender().getName(),
                saved.getReceiver().getName(),
                saved.getTopic().getTitle(),
                saved.getStatus()
        );
    }
}