package com.jobtrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_rounds", indexes = {
    @Index(name = "idx_interview_app", columnList = "application_id"),
    @Index(name = "idx_interview_date", columnList = "interview_date"),
    @Index(name = "idx_interview_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    @JsonIgnore
    private JobApplication application;

    @NotBlank(message = "Round name is required")
    @Column(name = "round_name", nullable = false, length = 150)
    private String roundName;

    @NotNull(message = "Round type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "round_type", nullable = false, length = 30)
    private InterviewType roundType = InterviewType.TECHNICAL;

    @NotNull(message = "Interview date and time is required")
    @Column(name = "interview_date", nullable = false)
    private LocalDateTime interviewDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 45;

    @Column(name = "interviewer_names", length = 200)
    private String interviewerNames;

    @Column(name = "meeting_link", length = 1024)
    private String meetingLink;

    @NotNull(message = "Interview status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(name = "feedback_notes", columnDefinition = "TEXT")
    private String feedbackNotes;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(name = "rating")
    private Integer rating;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public InterviewRound() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public void setApplication(JobApplication application) {
        this.application = application;
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
