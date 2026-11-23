package com.jobtrack.repository;

import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.WorkMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    @Query("SELECT j FROM JobApplication j WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           " LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           " LOWER(j.jobLocation) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:workMode IS NULL OR j.workMode = :workMode)")
    Page<JobApplication> searchAndFilter(
            @Param("search") String search,
            @Param("status") ApplicationStatus status,
            @Param("workMode") WorkMode workMode,
            Pageable pageable
    );

    long countByStatus(ApplicationStatus status);

    @Query("SELECT j.status, COUNT(j) FROM JobApplication j GROUP BY j.status")
    List<Object[]> countApplicationsByStatusGroup();
}
