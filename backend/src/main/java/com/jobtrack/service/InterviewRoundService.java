package com.jobtrack.service;

import com.jobtrack.dto.InterviewRoundRequest;
import com.jobtrack.dto.InterviewRoundResponse;

import java.util.List;

public interface InterviewRoundService {

    List<InterviewRoundResponse> getInterviewsByApplication(Long applicationId);

    List<InterviewRoundResponse> getUpcomingInterviews();

    InterviewRoundResponse scheduleInterview(Long applicationId, InterviewRoundRequest request);

    InterviewRoundResponse updateInterview(Long id, InterviewRoundRequest request);

    void deleteInterview(Long id);
}
