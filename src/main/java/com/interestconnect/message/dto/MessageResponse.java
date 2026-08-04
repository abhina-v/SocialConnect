package com.interestconnect.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {

    private Long id;

    private Long conversationId;

    private String senderName;

    private String content;

    private boolean read;
}