package com.jobtrack.dto;

import com.jobtrack.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApplicationDocumentRequest {

    @NotBlank(message = "Document name is required")
    private String documentName;

    @NotNull(message = "Document type is required")
    private DocumentType documentType = DocumentType.RESUME;

    private String documentUrl;
    private String notes;

    public ApplicationDocumentRequest() {
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
}
