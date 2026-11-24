package com.jobtrack.dto;

import com.jobtrack.entity.InterviewRound;
import com.jobtrack.entity.InterviewStatus;
import com.jobtrack.entity.InterviewType;

import java.time.Instant;
import java.time.LocalDateTime;

public class InterviewRoundResponse {

    private Long id;
    private Long applicationId;
    private String companyName;
    private String jobTitle;
    private String roundName;
    private InterviewType roundType;
    private LocalDateTime interviewDate;
    private Integer durationMinutes;
    private String interviewerNames;
    private String meetingLink;
    private InterviewStatus status;
    private String feedbackNotes;
    private Integer rating;
    private Instant createdAt;
    private Instant updatedAt;

    public InterviewRoundResponse() {
    }

    public static InterviewRoundResponse fromEntity(InterviewRound entity) {
        if (entity == null) return null;
        InterviewRoundResponse res = new InterviewRoundResponse();
        res.setId(entity.getId());
        if (entity.getApplication() != null) {
            res.setApplicationId(entity.getApplication().getId());
            res.setCompanyName(entity.getApplication().getCompanyName());
            res.setJobTitle(entity.getApplication().getJobTitle());
        }
        res.setRoundName(entity.getRoundName());
        res.setRoundType(entity.getRoundType());
        res.setInterviewDate(entity.getInterviewDate());
        res.setDurationMinutes(entity.getDurationMinutes());
        res.setInterviewerNames(entity.getInterviewerNames());
        res.setMeetingLink(entity.getMeetingLink());
        res.setStatus(entity.getStatus());
        res.setFeedbackNotes(entity.getFeedbackNotes());
        res.setRating(entity.getRating());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
