package com.interestconnect.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponse {

    private Long messageId;

    private Long conversationId;

    private String senderName;

    private String content;

    private LocalDateTime sentAt;
}