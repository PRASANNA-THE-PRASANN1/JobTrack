package com.jobtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtrack.dto.ApplicationStatsResponse;
import com.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.EmploymentType;
import com.jobtrack.entity.WorkMode;
import com.jobtrack.service.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobApplicationController.class)
class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private JobApplicationResponse mockResponse;
    private JobApplicationRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockResponse = new JobApplicationResponse();
        mockResponse.setId(1L);
        mockResponse.setCompanyName("Microsoft");
        mockResponse.setJobTitle("Senior Backend Engineer");
        mockResponse.setJobLocation("Redmond, WA");
        mockResponse.setWorkMode(WorkMode.HYBRID);
        mockResponse.setEmploymentType(EmploymentType.FULL_TIME);
        mockResponse.setStatus(ApplicationStatus.APPLIED);
        mockResponse.setSalaryCurrency("USD");
        mockResponse.setSalaryMin(new BigDecimal("160000"));
        mockResponse.setSalaryMax(new BigDecimal("210000"));
        mockResponse.setApplicationDate(LocalDate.now());

        mockRequest = new JobApplicationRequest();
        mockRequest.setCompanyName("Microsoft");
        mockRequest.setJobTitle("Senior Backend Engineer");
        mockRequest.setJobLocation("Redmond, WA");
        mockRequest.setWorkMode(WorkMode.HYBRID);
        mockRequest.setEmploymentType(EmploymentType.FULL_TIME);
        mockRequest.setStatus(ApplicationStatus.APPLIED);
        mockRequest.setApplicationDate(LocalDate.now());
    }

    @Test
    @DisplayName("GET /api/v1/applications returns 200 with paginated applications")
    void testGetAllApplications() throws Exception {
        when(applicationService.getApplications(any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockResponse)));

        mockMvc.perform(get("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content[0].companyName", is("Microsoft")))
                .andExpect(jsonPath("$.data.content[0].jobTitle", is("Senior Backend Engineer")));
    }

    @Test
    @DisplayName("GET /api/v1/applications/{id} returns 200 with application details")
    void testGetApplicationById() throws Exception {
        when(applicationService.getApplicationById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/applications/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.companyName", is("Microsoft")));
    }

    @Test
    @DisplayName("POST /api/v1/applications with valid payload returns 201 Created")
    void testCreateApplication() throws Exception {
        when(applicationService.createApplication(any(JobApplicationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.companyName", is("Microsoft")));
    }

    @Test
    @DisplayName("POST /api/v1/applications with missing company name returns 400 Bad Request")
    void testCreateApplicationValidationError() throws Exception {
        mockRequest.setCompanyName(""); // Invalid

        mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error", is("Validation Failed")));
    }

    @Test
    @DisplayName("DELETE /api/v1/applications/{id} returns 200 OK")
    void testDeleteApplication() throws Exception {
        doNothing().when(applicationService).deleteApplication(1L);

        mockMvc.perform(delete("/api/v1/applications/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Job application deleted successfully")));
    }

    @Test
    @DisplayName("GET /api/v1/applications/stats returns 200 with KPI metrics")
    void testGetStats() throws Exception {
        ApplicationStatsResponse stats = new ApplicationStatsResponse();
        stats.setTotalApplications(12);
        stats.setActiveApplications(8);
        stats.setAppliedCount(5);
        stats.setInterviewingCount(3);
        stats.setOfferedCount(1);
        stats.setRejectedCount(3);

        when(applicationService.getApplicationStats()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/applications/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalApplications", is(12)))
                .andExpect(jsonPath("$.data.activeApplications", is(8)))
                .andExpect(jsonPath("$.data.interviewingCount", is(3)));
    }
}
