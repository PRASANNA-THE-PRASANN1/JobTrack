package com.jobtrack.service;

import com.jobtrack.dto.InterviewRoundRequest;
import com.jobtrack.dto.InterviewRoundResponse;
import com.jobtrack.entity.*;
import com.jobtrack.repository.InterviewRoundRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.impl.InterviewRoundServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewRoundServiceTest {

    @Mock
    private InterviewRoundRepository interviewRepository;

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private TimelineService timelineService;

    @InjectMocks
    private InterviewRoundServiceImpl interviewService;

    private JobApplication testApp;
    private InterviewRound testRound;
    private InterviewRoundRequest testRequest;

    @BeforeEach
    void setUp() {
        testApp = new JobApplication();
        testApp.setId(10L);
        testApp.setCompanyName("Amazon");
        testApp.setJobTitle("Cloud Solutions Architect");
        testApp.setStatus(ApplicationStatus.APPLIED);

        testRound = new InterviewRound();
        testRound.setId(100L);
        testRound.setApplication(testApp);
        testRound.setRoundName("Technical Screening");
        testRound.setRoundType(InterviewType.TECHNICAL);
        testRound.setInterviewDate(LocalDateTime.now().plusDays(2));
        testRound.setStatus(InterviewStatus.SCHEDULED);

        testRequest = new InterviewRoundRequest();
        testRequest.setRoundName("Technical Screening");
        testRequest.setRoundType(InterviewType.TECHNICAL);
        testRequest.setInterviewDate(LocalDateTime.now().plusDays(2));
        testRequest.setDurationMinutes(60);
        testRequest.setInterviewerNames("Dave (Principal Architect)");
        testRequest.setMeetingLink("https://chime.aws/12345");
    }

    @Test
    @DisplayName("Scheduling interview saves round and auto-promotes application status to INTERVIEWING")
    void testScheduleInterviewAutoPromote() {
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(testApp));
        when(interviewRepository.save(any(InterviewRound.class))).thenReturn(testRound);

        InterviewRoundResponse response = interviewService.scheduleInterview(10L, testRequest);

        assertThat(response).isNotNull();
        assertThat(response.getRoundName()).isEqualTo("Technical Screening");
        assertThat(testApp.getStatus()).isEqualTo(ApplicationStatus.INTERVIEWING);
        verify(applicationRepository).save(testApp);
        verify(timelineService).logAutomatedEvent(eq(testApp), eq(TimelineEventType.INTERVIEW_SCHEDULED), anyString(), anyString());
    }

    @Test
    @DisplayName("Get upcoming interviews queries scheduled interviews")
    void testGetUpcomingInterviews() {
        when(interviewRepository.findUpcomingInterviews(any(), eq(InterviewStatus.SCHEDULED)))
                .thenReturn(List.of(testRound));

        List<InterviewRoundResponse> upcoming = interviewService.getUpcomingInterviews();

        assertThat(upcoming).hasSize(1);
        assertThat(upcoming.get(0).getCompanyName()).isEqualTo("Amazon");
    }

    @Test
    @DisplayName("Completing an interview logs completion event in timeline")
    void testCompleteInterviewLogsTimeline() {
        when(interviewRepository.findById(100L)).thenReturn(Optional.of(testRound));
        when(interviewRepository.save(any(InterviewRound.class))).thenAnswer(i -> i.getArgument(0));

        testRequest.setStatus(InterviewStatus.COMPLETED);
        testRequest.setRating(5);
        testRequest.setFeedbackNotes("Strong architecture knowledge and system design clarity.");

        InterviewRoundResponse updated = interviewService.updateInterview(100L, testRequest);

        assertThat(updated.getStatus()).isEqualTo(InterviewStatus.COMPLETED);
        assertThat(updated.getRating()).isEqualTo(5);
        verify(timelineService).logAutomatedEvent(eq(testApp), eq(TimelineEventType.INTERVIEW_COMPLETED), anyString(), anyString());
    }
}
