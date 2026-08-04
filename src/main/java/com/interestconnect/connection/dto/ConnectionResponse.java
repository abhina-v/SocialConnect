package com.interestconnect.connection.dto;

import com.interestconnect.connection.entity.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConnectionResponse {

    private Long id;

    private String senderName;

    private String receiverName;

    private String topicTitle;

    private ConnectionStatus status;
}