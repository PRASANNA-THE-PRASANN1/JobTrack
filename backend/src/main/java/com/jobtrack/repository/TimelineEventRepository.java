package com.jobtrack.repository;

import com.jobtrack.entity.TimelineEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineEventRepository extends JpaRepository<TimelineEvent, Long> {

    List<TimelineEvent> findByApplicationIdOrderByEventDateDesc(Long applicationId);
}
