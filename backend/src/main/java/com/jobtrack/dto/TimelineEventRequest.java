package com.jobtrack.dto;

import com.jobtrack.entity.TimelineEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TimelineEventRequest {

    @NotNull(message = "Event type is required")
    private TimelineEventType eventType = TimelineEventType.NOTE_ADDED;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    public TimelineEventRequest() {
    }

    public TimelineEventRequest(TimelineEventType eventType, String title, String description) {
        this.eventType = eventType;
        this.title = title;
        this.description = description;
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
}
