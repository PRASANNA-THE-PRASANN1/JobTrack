package com.jobtrack.service;

import com.jobtrack.dto.ApplicationDocumentRequest;
import com.jobtrack.dto.ApplicationDocumentResponse;

import java.util.List;

public interface ApplicationDocumentService {

    List<ApplicationDocumentResponse> getDocumentsByApplication(Long applicationId);

    ApplicationDocumentResponse addDocument(Long applicationId, ApplicationDocumentRequest request);

    void deleteDocument(Long id);
}
