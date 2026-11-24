package com.jobtrack.controller;

import com.jobtrack.dto.ApiResponse;
import com.jobtrack.dto.ApplicationDocumentRequest;
import com.jobtrack.dto.ApplicationDocumentResponse;
import com.jobtrack.service.ApplicationDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Documents", description = "Manage resumes, cover letters, and document links for applications")
public class ApplicationDocumentController {

    private final ApplicationDocumentService documentService;

    public ApplicationDocumentController(ApplicationDocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/api/v1/applications/{appId}/documents")
    @Operation(summary = "Get all documents attached to an application")
    public ResponseEntity<ApiResponse<List<ApplicationDocumentResponse>>> getDocuments(
            @PathVariable Long appId
    ) {
        List<ApplicationDocumentResponse> docs = documentService.getDocumentsByApplication(appId);
        return ResponseEntity.ok(ApiResponse.success(docs));
    }

    @PostMapping("/api/v1/applications/{appId}/documents")
    @Operation(summary = "Attach a new document or link to an application")
    public ResponseEntity<ApiResponse<ApplicationDocumentResponse>> addDocument(
            @PathVariable Long appId,
            @Valid @RequestBody ApplicationDocumentRequest request
    ) {
        ApplicationDocumentResponse created = documentService.addDocument(appId, request);
        return new ResponseEntity<>(ApiResponse.success("Document attached successfully", created), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/documents/{id}")
    @Operation(summary = "Delete an attached document")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}
