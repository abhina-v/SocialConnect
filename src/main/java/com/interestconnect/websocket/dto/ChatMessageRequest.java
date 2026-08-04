package com.interestconnect.websocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {

    private Long conversationId;

    private String content;
}