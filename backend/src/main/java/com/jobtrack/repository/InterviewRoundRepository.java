package com.jobtrack.repository;

import com.jobtrack.entity.InterviewRound;
import com.jobtrack.entity.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByApplicationIdOrderByInterviewDateAsc(Long applicationId);

    @Query("SELECT i FROM InterviewRound i JOIN FETCH i.application a " +
           "WHERE i.interviewDate >= :fromDate AND i.status = :status " +
           "ORDER BY i.interviewDate ASC")
    List<InterviewRound> findUpcomingInterviews(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("status") InterviewStatus status
    );
}
