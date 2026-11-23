package com.jobtrack.service;

import com.jobtrack.dto.ApplicationStatsResponse;
import com.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.WorkMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationService {

    Page<JobApplicationResponse> getApplications(String search, ApplicationStatus status, WorkMode workMode, Pageable pageable);

    JobApplicationResponse getApplicationById(Long id);

    JobApplicationResponse createApplication(JobApplicationRequest request);

    JobApplicationResponse updateApplication(Long id, JobApplicationRequest request);

    void deleteApplication(Long id);

    ApplicationStatsResponse getApplicationStats();
}
