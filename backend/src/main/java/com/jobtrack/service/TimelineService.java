package com.jobtrack.service;

import com.jobtrack.dto.TimelineEventRequest;
import com.jobtrack.dto.TimelineEventResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.TimelineEventType;

import java.util.List;

public interface TimelineService {

    List<TimelineEventResponse> getTimelineEvents(Long applicationId);

    TimelineEventResponse addTimelineEvent(Long applicationId, TimelineEventRequest request);

    void logAutomatedEvent(JobApplication application, TimelineEventType eventType, String title, String description);
}
