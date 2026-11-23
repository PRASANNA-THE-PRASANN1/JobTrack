package com.jobtrack.service.impl;

import com.jobtrack.dto.ApplicationStatsResponse;
import com.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.WorkMode;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.JobApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationServiceImpl(JobApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> getApplications(String search, ApplicationStatus status, WorkMode workMode, Pageable pageable) {
        Page<JobApplication> page = repository.searchAndFilter(search, status, workMode, pageable);
        return page.map(JobApplicationResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getApplicationById(Long id) {
        JobApplication application = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id));
        return JobApplicationResponse.fromEntity(application);
    }

    @Override
    public JobApplicationResponse createApplication(JobApplicationRequest request) {
        JobApplication application = new JobApplication();
        mapDtoToEntity(request, application);
        JobApplication saved = repository.save(application);
        return JobApplicationResponse.fromEntity(saved);
    }

    @Override
    public JobApplicationResponse updateApplication(Long id, JobApplicationRequest request) {
        JobApplication application = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id));

        mapDtoToEntity(request, application);
        JobApplication updated = repository.save(application);
        return JobApplicationResponse.fromEntity(updated);
    }

    @Override
    public void deleteApplication(Long id) {
        JobApplication application = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id));
        repository.delete(application);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationStatsResponse getApplicationStats() {
        ApplicationStatsResponse stats = new ApplicationStatsResponse();
        long total = repository.count();
        stats.setTotalApplications(total);

        Map<String, Long> statusCounts = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusCounts.put(status.name(), 0L);
        }

        List<Object[]> groupCounts = repository.countApplicationsByStatusGroup();
        for (Object[] row : groupCounts) {
            ApplicationStatus status = (ApplicationStatus) row[0];
            Long count = (Long) row[1];
            if (status != null) {
                statusCounts.put(status.name(), count);
            }
        }

        stats.setByStatus(statusCounts);
        stats.setSavedCount(statusCounts.getOrDefault(ApplicationStatus.SAVED.name(), 0L));
        stats.setAppliedCount(statusCounts.getOrDefault(ApplicationStatus.APPLIED.name(), 0L));
        stats.setScreeningCount(statusCounts.getOrDefault(ApplicationStatus.SCREENING.name(), 0L));
        stats.setInterviewingCount(statusCounts.getOrDefault(ApplicationStatus.INTERVIEWING.name(), 0L));
        stats.setOfferedCount(statusCounts.getOrDefault(ApplicationStatus.OFFERED.name(), 0L));
        stats.setRejectedCount(statusCounts.getOrDefault(ApplicationStatus.REJECTED.name(), 0L));
        stats.setWithdrawnCount(statusCounts.getOrDefault(ApplicationStatus.WITHDRAWN.name(), 0L));

        // Active applications: Applied + Screening + Interviewing
        long active = stats.getAppliedCount() + stats.getScreeningCount() + stats.getInterviewingCount();
        stats.setActiveApplications(active);

        return stats;
    }

    private void mapDtoToEntity(JobApplicationRequest dto, JobApplication entity) {
        entity.setCompanyName(dto.getCompanyName().trim());
        entity.setJobTitle(dto.getJobTitle().trim());
        entity.setJobLocation(dto.getJobLocation() != null ? dto.getJobLocation().trim() : null);
        entity.setWorkMode(dto.getWorkMode() != null ? dto.getWorkMode() : WorkMode.REMOTE);
        entity.setEmploymentType(dto.getEmploymentType() != null ? dto.getEmploymentType() : com.jobtrack.entity.EmploymentType.FULL_TIME);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : ApplicationStatus.APPLIED);
        entity.setSalaryCurrency(dto.getSalaryCurrency() != null ? dto.getSalaryCurrency().trim().toUpperCase() : "USD");
        entity.setSalaryMin(dto.getSalaryMin());
        entity.setSalaryMax(dto.getSalaryMax());
        entity.setJobUrl(dto.getJobUrl() != null ? dto.getJobUrl().trim() : null);
        entity.setApplicationDate(dto.getApplicationDate() != null ? dto.getApplicationDate() : java.time.LocalDate.now());
        entity.setJobDescription(dto.getJobDescription());
        entity.setNotes(dto.getNotes());
        entity.setContactPerson(dto.getContactPerson() != null ? dto.getContactPerson().trim() : null);
        entity.setContactEmail(dto.getContactEmail() != null ? dto.getContactEmail().trim() : null);
    }
}
