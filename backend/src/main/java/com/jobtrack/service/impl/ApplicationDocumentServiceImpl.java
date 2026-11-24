package com.jobtrack.service.impl;

import com.jobtrack.dto.ApplicationDocumentRequest;
import com.jobtrack.dto.ApplicationDocumentResponse;
import com.jobtrack.entity.ApplicationDocument;
import com.jobtrack.entity.DocumentType;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.TimelineEventType;
import com.jobtrack.exception.ResourceNotFoundException;
import com.jobtrack.repository.ApplicationDocumentRepository;
import com.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.service.ApplicationDocumentService;
import com.jobtrack.service.TimelineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationDocumentServiceImpl implements ApplicationDocumentService {

    private final ApplicationDocumentRepository documentRepository;
    private final JobApplicationRepository applicationRepository;
    private final TimelineService timelineService;

    public ApplicationDocumentServiceImpl(
            ApplicationDocumentRepository documentRepository,
            JobApplicationRepository applicationRepository,
            TimelineService timelineService
    ) {
        this.documentRepository = documentRepository;
        this.applicationRepository = applicationRepository;
        this.timelineService = timelineService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDocumentResponse> getDocumentsByApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("JobApplication", "id", applicationId);
        }
        return documentRepository.findByApplicationIdOrderByCreatedAtDesc(applicationId)
                .stream()
                .map(ApplicationDocumentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDocumentResponse addDocument(Long applicationId, ApplicationDocumentRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", applicationId));

        ApplicationDocument doc = new ApplicationDocument(
                application,
                request.getDocumentName().trim(),
                request.getDocumentType() != null ? request.getDocumentType() : DocumentType.RESUME,
                request.getDocumentUrl(),
                request.getNotes()
        );

        ApplicationDocument saved = documentRepository.save(doc);

        timelineService.logAutomatedEvent(
                application,
                TimelineEventType.DOCUMENT_ATTACHED,
                "Document Attached: " + saved.getDocumentName(),
                "Type: " + saved.getDocumentType()
        );

        return ApplicationDocumentResponse.fromEntity(saved);
    }

    @Override
    public void deleteDocument(Long id) {
        ApplicationDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApplicationDocument", "id", id));
        documentRepository.delete(doc);
    }
}
