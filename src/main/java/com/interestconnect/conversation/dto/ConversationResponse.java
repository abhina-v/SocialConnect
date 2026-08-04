package com.interestconnect.conversation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConversationResponse {

    private Long id;

    private Long connectionRequestId;

    private String user1Name;

    private String user2Name;
}