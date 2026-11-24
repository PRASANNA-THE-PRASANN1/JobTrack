package com.jobtrack.dto;

import com.jobtrack.entity.InterviewStatus;
import com.jobtrack.entity.InterviewType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class InterviewRoundRequest {

    @NotBlank(message = "Round name is required")
    private String roundName;

    @NotNull(message = "Round type is required")
    private InterviewType roundType = InterviewType.TECHNICAL;

    @NotNull(message = "Interview date is required")
    private LocalDateTime interviewDate;

    private Integer durationMinutes = 45;
    private String interviewerNames;
    private String meetingLink;
    private InterviewStatus status = InterviewStatus.SCHEDULED;
    private String feedbackNotes;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    public InterviewRoundRequest() {
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public InterviewType getRoundType() {
        return roundType;
    }

    public void setRoundType(InterviewType roundType) {
        this.roundType = roundType;
    }

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getInterviewerNames() {
        return interviewerNames;
    }

    public void setInterviewerNames(String interviewerNames) {
        this.interviewerNames = interviewerNames;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public String getFeedbackNotes() {
        return feedbackNotes;
    }

    public void setFeedbackNotes(String feedbackNotes) {
        this.feedbackNotes = feedbackNotes;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
