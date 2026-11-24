package com.jobtrack.dto;

import com.jobtrack.entity.ApplicationDocument;
import com.jobtrack.entity.DocumentType;

import java.time.Instant;

public class ApplicationDocumentResponse {

    private Long id;
    private Long applicationId;
    private String documentName;
    private DocumentType documentType;
    private String documentUrl;
    private String notes;
    private Instant createdAt;

    public ApplicationDocumentResponse() {
    }

    public static ApplicationDocumentResponse fromEntity(ApplicationDocument entity) {
        if (entity == null) return null;
        ApplicationDocumentResponse res = new ApplicationDocumentResponse();
        res.setId(entity.getId());
        if (entity.getApplication() != null) {
            res.setApplicationId(entity.getApplication().getId());
        }
        res.setDocumentName(entity.getDocumentName());
        res.setDocumentType(entity.getDocumentType());
        res.setDocumentUrl(entity.getDocumentUrl());
        res.setNotes(entity.getNotes());
        res.setCreatedAt(entity.getCreatedAt());
        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
