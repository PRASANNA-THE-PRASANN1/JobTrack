package com.jobtrack.dto;

import com.jobtrack.entity.TimelineEvent;
import com.jobtrack.entity.TimelineEventType;

import java.time.Instant;

public class TimelineEventResponse {

    private Long id;
    private Long applicationId;
    private TimelineEventType eventType;
    private String title;
    private String description;
    private Instant eventDate;
    private Instant createdAt;

    public TimelineEventResponse() {
    }

    public static TimelineEventResponse fromEntity(TimelineEvent entity) {
        if (entity == null) return null;
        TimelineEventResponse res = new TimelineEventResponse();
        res.setId(entity.getId());
        if (entity.getApplication() != null) {
            res.setApplicationId(entity.getApplication().getId());
        }
        res.setEventType(entity.getEventType());
        res.setTitle(entity.getTitle());
        res.setDescription(entity.getDescription());
        res.setEventDate(entity.getEventDate());
        res.setCreatedAt(entity.getCreatedAt());
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
