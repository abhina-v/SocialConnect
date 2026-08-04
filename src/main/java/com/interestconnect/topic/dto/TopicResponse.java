package com.interestconnect.topic.dto;

import com.interestconnect.topic.entity.TopicStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopicResponse {

    private Long id;

    private String title;

    private String description;

    private String tags;

    private TopicStatus status;

    private String createdBy;

}