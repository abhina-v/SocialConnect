package com.interestconnect.topic.entity;

import com.interestconnect.common.entity.BaseEntity;
import com.interestconnect.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
public class Topic extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String tags;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TopicStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

}