package com.interestconnect.topic.repository;

import com.interestconnect.topic.entity.Topic;
import com.interestconnect.topic.entity.TopicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByStatus(TopicStatus status);

    List<Topic> findByTagsContainingIgnoreCase(String tag);

}