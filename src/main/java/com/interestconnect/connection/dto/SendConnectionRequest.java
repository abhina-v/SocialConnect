package com.interestconnect.connection.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendConnectionRequest {

    @NotNull(message = "Topic ID is required")
    private Long topicId;
}