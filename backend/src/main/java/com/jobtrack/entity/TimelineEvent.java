package com.jobtrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "timeline_events", indexes = {
    @Index(name = "idx_timeline_app", columnList = "application_id"),
    @Index(name = "idx_timeline_date", columnList = "event_date")
})
@EntityListeners(AuditingEntityListener.class)
public class TimelineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    @JsonIgnore
    private JobApplication application;

    @NotNull(message = "Event type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private TimelineEventType eventType = TimelineEventType.NOTE_ADDED;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private Instant eventDate = Instant.now();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public TimelineEvent() {
    }

    public TimelineEvent(JobApplication application, TimelineEventType eventType, String title, String description) {
        this.application = application;
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.eventDate = Instant.now();
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

    public TimelineEventType getEventType() {
        return eventType;
    }

    public void setEventType(TimelineEventType eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
