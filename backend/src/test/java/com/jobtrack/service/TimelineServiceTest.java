package com.jobtrack.service;

import com.jobtrack.dto.TimelineEventRequest;
import com.jobtrack.dto.TimelineEventResponse;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.TimelineEvent;
import com.jobtrack.entity.TimelineEventType;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.repository.TimelineEventRepository;
import com.jobtrack.service.impl.TimelineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimelineServiceTest {

    @Mock
    private TimelineEventRepository timelineRepository;

    @Mock
    private JobApplicationRepository applicationRepository;

    @InjectMocks
    private TimelineServiceImpl timelineService;

    private JobApplication testApp;
    private TimelineEvent testEvent;

    @BeforeEach
    void setUp() {
        testApp = new JobApplication();
        testApp.setId(5L);
        testApp.setCompanyName("Meta");
        testApp.setJobTitle("Software Engineer");

        testEvent = new TimelineEvent(testApp, TimelineEventType.NOTE_ADDED, "Follow-up Sent", "Sent email to recruiter");
        testEvent.setId(50L);
    }

    @Test
    @DisplayName("Get timeline events returns chronological entries for application")
    void testGetTimelineEvents() {
        when(applicationRepository.existsById(5L)).thenReturn(true);
        when(timelineRepository.findByApplicationIdOrderByEventDateDesc(5L)).thenReturn(List.of(testEvent));

        List<TimelineEventResponse> events = timelineService.getTimelineEvents(5L);

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getTitle()).isEqualTo("Follow-up Sent");
        assertThat(events.get(0).getEventType()).isEqualTo(TimelineEventType.NOTE_ADDED);
    }

    @Test
    @DisplayName("Add manual timeline event saves event")
    void testAddTimelineEvent() {
        when(applicationRepository.findById(5L)).thenReturn(Optional.of(testApp));
        when(timelineRepository.save(any(TimelineEvent.class))).thenReturn(testEvent);

        TimelineEventRequest req = new TimelineEventRequest(TimelineEventType.NOTE_ADDED, "Follow-up Sent", "Sent email");
        TimelineEventResponse res = timelineService.addTimelineEvent(5L, req);

        assertThat(res).isNotNull();
        assertThat(res.getTitle()).isEqualTo("Follow-up Sent");
        verify(timelineRepository).save(any(TimelineEvent.class));
    }
}
