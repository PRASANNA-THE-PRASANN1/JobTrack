package com.jobtrack.service;

import com.jobtrack.dto.ApplicationStatsResponse;
import com.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.WorkMode;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.impl.JobApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @Mock
    private TimelineService timelineService;

    @InjectMocks
    private JobApplicationServiceImpl service;

    private JobApplication testApp;
    private JobApplicationRequest testRequest;

    @BeforeEach
    void setUp() {
        testApp = new JobApplication();
        testApp.setId(1L);
        testApp.setCompanyName("Google");
        testApp.setJobTitle("Software Engineer");
        testApp.setJobLocation("Mountain View, CA");
        testApp.setWorkMode(WorkMode.HYBRID);
        testApp.setStatus(ApplicationStatus.APPLIED);
        testApp.setSalaryCurrency("USD");
        testApp.setSalaryMin(new BigDecimal("150000"));
        testApp.setSalaryMax(new BigDecimal("190000"));
        testApp.setApplicationDate(LocalDate.now());

        testRequest = new JobApplicationRequest();
        testRequest.setCompanyName("Google");
        testRequest.setJobTitle("Software Engineer");
        testRequest.setJobLocation("Mountain View, CA");
        testRequest.setWorkMode(WorkMode.HYBRID);
        testRequest.setStatus(ApplicationStatus.APPLIED);
        testRequest.setSalaryMin(new BigDecimal("150000"));
        testRequest.setSalaryMax(new BigDecimal("190000"));
        testRequest.setApplicationDate(LocalDate.now());
    }

    @Test
    @DisplayName("Create application should save entity and return DTO")
    void testCreateApplication() {
        when(repository.save(any(JobApplication.class))).thenReturn(testApp);

        JobApplicationResponse response = service.createApplication(testRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCompanyName()).isEqualTo("Google");
        assertThat(response.getJobTitle()).isEqualTo("Software Engineer");
        verify(repository, times(1)).save(any(JobApplication.class));
    }

    @Test
    @DisplayName("Get application by ID returns matching entity")
    void testGetApplicationById() {
        when(repository.findById(1L)).thenReturn(Optional.of(testApp));

        JobApplicationResponse response = service.getApplicationById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCompanyName()).isEqualTo("Google");
    }

    @Test
    @DisplayName("Get application by non-existing ID throws ResourceNotFoundException")
    void testGetApplicationByIdNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getApplicationById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("JobApplication not found with id : '999'");
    }

    @Test
    @DisplayName("Update application updates existing entity fields")
    void testUpdateApplication() {
        when(repository.findById(1L)).thenReturn(Optional.of(testApp));
        when(repository.save(any(JobApplication.class))).thenReturn(testApp);

        testRequest.setStatus(ApplicationStatus.INTERVIEWING);
        JobApplicationResponse response = service.updateApplication(1L, testRequest);

        assertThat(response).isNotNull();
        verify(repository).save(testApp);
    }

    @Test
    @DisplayName("Delete application deletes entity when present")
    void testDeleteApplication() {
        when(repository.findById(1L)).thenReturn(Optional.of(testApp));
        doNothing().when(repository).delete(testApp);

        service.deleteApplication(1L);

        verify(repository, times(1)).delete(testApp);
    }

    @Test
    @DisplayName("Get application stats computes active and total counts accurately")
    void testGetApplicationStats() {
        when(repository.count()).thenReturn(5L);
        List<Object[]> mockGroup = new ArrayList<>();
        mockGroup.add(new Object[]{ApplicationStatus.APPLIED, 3L});
        mockGroup.add(new Object[]{ApplicationStatus.INTERVIEWING, 1L});
        mockGroup.add(new Object[]{ApplicationStatus.OFFERED, 1L});

        when(repository.countApplicationsByStatusGroup()).thenReturn(mockGroup);

        ApplicationStatsResponse stats = service.getApplicationStats();

        assertThat(stats.getTotalApplications()).isEqualTo(5L);
        assertThat(stats.getAppliedCount()).isEqualTo(3L);
        assertThat(stats.getInterviewingCount()).isEqualTo(1L);
        assertThat(stats.getOfferedCount()).isEqualTo(1L);
        assertThat(stats.getActiveApplications()).isEqualTo(4L); // Applied (3) + Interviewing (1)
    }
}
