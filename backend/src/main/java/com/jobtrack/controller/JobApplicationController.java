package com.jobtrack.controller;

import com.jobtrack.dto.ApiResponse;
import com.jobtrack.dto.ApplicationStatsResponse;
import com.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.WorkMode;
import com.jobtrack.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Job Applications", description = "Endpoints for managing job applications, status transitions, and KPIs")
public class JobApplicationController {

    private final JobApplicationService applicationService;

    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    @Operation(summary = "List and search job applications with pagination and filters")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> getAllApplications(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) WorkMode workMode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "applicationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<JobApplicationResponse> result = applicationService.getApplications(search, status, workMode, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application details by ID")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getApplicationById(@PathVariable Long id) {
        JobApplicationResponse response = applicationService.getApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    @Operation(summary = "Create a new job application")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> createApplication(
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse created = applicationService.createApplication(request);
        return new ResponseEntity<>(ApiResponse.success("Job application created successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing job application")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse updated = applicationService.updateApplication(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job application updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job application by ID")
    public ResponseEntity<ApiResponse<Void>> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Job application deleted successfully", null));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get aggregated application KPI counts and status distribution")
    public ResponseEntity<ApiResponse<ApplicationStatsResponse>> getStats() {
        ApplicationStatsResponse stats = applicationService.getApplicationStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
