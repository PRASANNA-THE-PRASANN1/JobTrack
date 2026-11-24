package com.jobtrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "application_documents", indexes = {
    @Index(name = "idx_doc_app", columnList = "application_id")
})
@EntityListeners(AuditingEntityListener.class)
public class ApplicationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    @JsonIgnore
    private JobApplication application;

    @NotBlank(message = "Document name is required")
    @Column(name = "document_name", nullable = false, length = 150)
    private String documentName;

    @NotNull(message = "Document type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 30)
    private DocumentType documentType = DocumentType.RESUME;

    @Column(name = "document_url", length = 1024)
    private String documentUrl;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ApplicationDocument() {
    }

    public ApplicationDocument(JobApplication application, String documentName, DocumentType documentType, String documentUrl, String notes) {
        this.application = application;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentUrl = documentUrl;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public void setApplication(JobApplication application) {
        this.application = application;
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
