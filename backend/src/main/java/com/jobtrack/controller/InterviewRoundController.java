package com.jobtrack.controller;

import com.jobtrack.dto.ApiResponse;
import com.jobtrack.dto.InterviewRoundRequest;
import com.jobtrack.dto.InterviewRoundResponse;
import com.jobtrack.service.InterviewRoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Interviews", description = "Endpoints for scheduling, tracking, and reviewing interview rounds")
public class InterviewRoundController {

    private final InterviewRoundService interviewService;

    public InterviewRoundController(InterviewRoundService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping("/api/v1/applications/{appId}/interviews")
    @Operation(summary = "Get all interview rounds for a specific job application")
    public ResponseEntity<ApiResponse<List<InterviewRoundResponse>>> getInterviewsByApplication(
            @PathVariable Long appId
    ) {
        List<InterviewRoundResponse> interviews = interviewService.getInterviewsByApplication(appId);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @PostMapping("/api/v1/applications/{appId}/interviews")
    @Operation(summary = "Schedule a new interview round for an application")
    public ResponseEntity<ApiResponse<InterviewRoundResponse>> scheduleInterview(
            @PathVariable Long appId,
            @Valid @RequestBody InterviewRoundRequest request
    ) {
        InterviewRoundResponse created = interviewService.scheduleInterview(appId, request);
        return new ResponseEntity<>(ApiResponse.success("Interview scheduled successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/interviews/{id}")
    @Operation(summary = "Update interview round details, status, feedback, or rating")
    public ResponseEntity<ApiResponse<InterviewRoundResponse>> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRoundRequest request
    ) {
        InterviewRoundResponse updated = interviewService.updateInterview(id, request);
        return ResponseEntity.ok(ApiResponse.success("Interview updated successfully", updated));
    }

    @DeleteMapping("/api/v1/interviews/{id}")
    @Operation(summary = "Delete an interview round")
    public ResponseEntity<ApiResponse<Void>> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.ok(ApiResponse.success("Interview round deleted successfully", null));
    }

    @GetMapping("/api/v1/interviews/upcoming")
    @Operation(summary = "Get all upcoming scheduled interviews across all applications")
    public ResponseEntity<ApiResponse<List<InterviewRoundResponse>>> getUpcomingInterviews() {
        List<InterviewRoundResponse> upcoming = interviewService.getUpcomingInterviews();
        return ResponseEntity.ok(ApiResponse.success(upcoming));
    }
}
