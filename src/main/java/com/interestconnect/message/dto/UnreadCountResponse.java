package com.interestconnect.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnreadCountResponse {

    private long unreadCount;
}