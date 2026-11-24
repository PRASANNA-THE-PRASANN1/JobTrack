package com.jobtrack.service.impl;

import com.jobtrack.dto.TimelineEventRequest;
import com.jobtrack.dto.TimelineEventResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.TimelineEvent;
import com.jobtrack.entity.TimelineEventType;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.repository.TimelineEventRepository;
import com.jobtrack.service.TimelineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimelineServiceImpl implements TimelineService {

    private final TimelineEventRepository timelineRepository;
    private final JobApplicationRepository applicationRepository;

    public TimelineServiceImpl(TimelineEventRepository timelineRepository, JobApplicationRepository applicationRepository) {
        this.timelineRepository = timelineRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimelineEventResponse> getTimelineEvents(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("JobApplication", "id", applicationId);
        }
        return timelineRepository.findByApplicationIdOrderByEventDateDesc(applicationId)
                .stream()
                .map(TimelineEventResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TimelineEventResponse addTimelineEvent(Long applicationId, TimelineEventRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", applicationId));

        TimelineEvent event = new TimelineEvent(
                application,
                request.getEventType(),
                request.getTitle(),
                request.getDescription()
        );

        TimelineEvent saved = timelineRepository.save(event);
        return TimelineEventResponse.fromEntity(saved);
    }

    @Override
    public void logAutomatedEvent(JobApplication application, TimelineEventType eventType, String title, String description) {
        TimelineEvent event = new TimelineEvent(application, eventType, title, description);
        timelineRepository.save(event);
    }
}
