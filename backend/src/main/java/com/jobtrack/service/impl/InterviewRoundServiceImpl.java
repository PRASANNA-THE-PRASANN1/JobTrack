package com.jobtrack.service.impl;

import com.jobtrack.dto.InterviewRoundRequest;
import com.jobtrack.dto.InterviewRoundResponse;
import com.jobtrack.entity.*;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.repository.InterviewRoundRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.InterviewRoundService;
import com.jobtrack.service.TimelineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterviewRoundServiceImpl implements InterviewRoundService {

    private final InterviewRoundRepository interviewRepository;
    private final JobApplicationRepository applicationRepository;
    private final TimelineService timelineService;

    public InterviewRoundServiceImpl(
            InterviewRoundRepository interviewRepository,
            JobApplicationRepository applicationRepository,
            TimelineService timelineService
    ) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.timelineService = timelineService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewRoundResponse> getInterviewsByApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("JobApplication", "id", applicationId);
        }
        return interviewRepository.findByApplicationIdOrderByInterviewDateAsc(applicationId)
                .stream()
                .map(InterviewRoundResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewRoundResponse> getUpcomingInterviews() {
        LocalDateTime now = LocalDateTime.now();
        return interviewRepository.findUpcomingInterviews(now, InterviewStatus.SCHEDULED)
                .stream()
                .map(InterviewRoundResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public InterviewRoundResponse scheduleInterview(Long applicationId, InterviewRoundRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", applicationId));

        InterviewRound round = new InterviewRound();
        round.setApplication(application);
        round.setRoundName(request.getRoundName().trim());
        round.setRoundType(request.getRoundType() != null ? request.getRoundType() : InterviewType.TECHNICAL);
        round.setInterviewDate(request.getInterviewDate());
        round.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 45);
        round.setInterviewerNames(request.getInterviewerNames());
        round.setMeetingLink(request.getMeetingLink());
        round.setStatus(request.getStatus() != null ? request.getStatus() : InterviewStatus.SCHEDULED);
        round.setFeedbackNotes(request.getFeedbackNotes());
        round.setRating(request.getRating());

        InterviewRound saved = interviewRepository.save(round);

        // Auto-promote application to INTERVIEWING if currently in APPLIED or SAVED or SCREENING
        if (application.getStatus() == ApplicationStatus.SAVED ||
            application.getStatus() == ApplicationStatus.APPLIED ||
            application.getStatus() == ApplicationStatus.SCREENING) {
            application.setStatus(ApplicationStatus.INTERVIEWING);
            applicationRepository.save(application);
        }

        // Log timeline event
        timelineService.logAutomatedEvent(
                application,
                TimelineEventType.INTERVIEW_SCHEDULED,
                "Interview Scheduled: " + saved.getRoundName(),
                "Round Type: " + saved.getRoundType() + " scheduled for " + saved.getInterviewDate() +
                (saved.getInterviewerNames() != null ? " with " + saved.getInterviewerNames() : "")
        );

        return InterviewRoundResponse.fromEntity(saved);
    }

    @Override
    public InterviewRoundResponse updateInterview(Long id, InterviewRoundRequest request) {
        InterviewRound round = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewRound", "id", id));

        InterviewStatus previousStatus = round.getStatus();

        round.setRoundName(request.getRoundName().trim());
        round.setRoundType(request.getRoundType() != null ? request.getRoundType() : round.getRoundType());
        round.setInterviewDate(request.getInterviewDate() != null ? request.getInterviewDate() : round.getInterviewDate());
        round.setDurationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : round.getDurationMinutes());
        round.setInterviewerNames(request.getInterviewerNames());
        round.setMeetingLink(request.getMeetingLink());
        round.setStatus(request.getStatus() != null ? request.getStatus() : round.getStatus());
        round.setFeedbackNotes(request.getFeedbackNotes());
        round.setRating(request.getRating());

        InterviewRound updated = interviewRepository.save(round);

        // Check if completed
        if (previousStatus != InterviewStatus.COMPLETED && updated.getStatus() == InterviewStatus.COMPLETED) {
            timelineService.logAutomatedEvent(
                    round.getApplication(),
                    TimelineEventType.INTERVIEW_COMPLETED,
                    "Interview Completed: " + updated.getRoundName(),
                    "Completed round " + updated.getRoundType() +
                    (updated.getRating() != null ? " (Rated: " + updated.getRating() + "/5)" : "")
            );
        }

        return InterviewRoundResponse.fromEntity(updated);
    }

    @Override
    public void deleteInterview(Long id) {
        InterviewRound round = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewRound", "id", id));
        interviewRepository.delete(round);
    }
}
