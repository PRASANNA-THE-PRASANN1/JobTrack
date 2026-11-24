package com.jobtrack.controller;

import com.jobtrack.dto.ApiResponse;
import com.jobtrack.dto.TimelineEventRequest;
import com.jobtrack.dto.TimelineEventResponse;
import com.jobtrack.service.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications/{appId}/timeline")
@Tag(name = "Timeline", description = "Activity timeline, status audit trails, and manual log notes")
public class TimelineController {

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @GetMapping
    @Operation(summary = "Get chronological activity timeline for an application")
    public ResponseEntity<ApiResponse<List<TimelineEventResponse>>> getTimeline(
            @PathVariable Long appId
    ) {
        List<TimelineEventResponse> events = timelineService.getTimelineEvents(appId);
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @PostMapping
    @Operation(summary = "Add a manual timeline entry or log note")
    public ResponseEntity<ApiResponse<TimelineEventResponse>> addTimelineEvent(
            @PathVariable Long appId,
            @Valid @RequestBody TimelineEventRequest request
    ) {
        TimelineEventResponse created = timelineService.addTimelineEvent(appId, request);
        return new ResponseEntity<>(ApiResponse.success("Timeline event added successfully", created), HttpStatus.CREATED);
    }
}
