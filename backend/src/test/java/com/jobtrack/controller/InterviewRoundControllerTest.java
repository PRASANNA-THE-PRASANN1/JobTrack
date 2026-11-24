package com.jobtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtrack.dto.InterviewRoundRequest;
import com.jobtrack.dto.InterviewRoundResponse;
import com.jobtrack.entity.InterviewStatus;
import com.jobtrack.entity.InterviewType;
import com.jobtrack.service.InterviewRoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InterviewRoundController.class)
class InterviewRoundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewRoundService interviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private InterviewRoundResponse mockResponse;
    private InterviewRoundRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockResponse = new InterviewRoundResponse();
        mockResponse.setId(1L);
        mockResponse.setApplicationId(10L);
        mockResponse.setCompanyName("Apple");
        mockResponse.setRoundName("iOS Core Engineering");
        mockResponse.setRoundType(InterviewType.TECHNICAL);
        mockResponse.setStatus(InterviewStatus.SCHEDULED);
        mockResponse.setInterviewDate(LocalDateTime.now().plusDays(1));

        mockRequest = new InterviewRoundRequest();
        mockRequest.setRoundName("iOS Core Engineering");
        mockRequest.setRoundType(InterviewType.TECHNICAL);
        mockRequest.setInterviewDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("GET /api/v1/applications/{appId}/interviews returns 200 with rounds")
    void testGetInterviews() throws Exception {
        when(interviewService.getInterviewsByApplication(10L)).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/v1/applications/10/interviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].roundName", is("iOS Core Engineering")));
    }

    @Test
    @DisplayName("GET /api/v1/interviews/upcoming returns 200 with upcoming rounds")
    void testGetUpcoming() throws Exception {
        when(interviewService.getUpcomingInterviews()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/v1/interviews/upcoming")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data[0].companyName", is("Apple")));
    }

    @Test
    @DisplayName("POST /api/v1/applications/{appId}/interviews returns 201 Created")
    void testScheduleInterview() throws Exception {
        when(interviewService.scheduleInterview(eq(10L), any(InterviewRoundRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/applications/10/interviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.roundName", is("iOS Core Engineering")));
    }
}
