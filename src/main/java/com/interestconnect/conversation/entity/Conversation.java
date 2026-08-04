package com.interestconnect.conversation.entity;

import com.interestconnect.common.entity.BaseEntity;
import com.interestconnect.connection.entity.ConnectionRequest;
import com.interestconnect.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "conversations")
public class Conversation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_request_id", nullable = false, unique = true)
    private ConnectionRequest connectionRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    public ConnectionRequest getConnectionRequest() {
        return connectionRequest;
    }

    public void setConnectionRequest(ConnectionRequest connectionRequest) {
        this.connectionRequest = connectionRequest;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}