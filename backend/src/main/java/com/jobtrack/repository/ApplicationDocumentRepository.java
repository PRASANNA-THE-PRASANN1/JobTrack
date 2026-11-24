package com.jobtrack.repository;

import com.jobtrack.entity.ApplicationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {

    List<ApplicationDocument> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);
}
