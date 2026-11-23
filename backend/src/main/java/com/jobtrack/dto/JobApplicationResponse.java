package com.jobtrack.dto;

import com.jobtrack.entity.ApplicationStatus;
import com.jobtrack.entity.EmploymentType;
import com.jobtrack.entity.JobApplication;
import com.jobtrack.entity.WorkMode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class JobApplicationResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private String jobLocation;
    private WorkMode workMode;
    private EmploymentType employmentType;
    private ApplicationStatus status;
    private String salaryCurrency;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String jobUrl;
    private LocalDate applicationDate;
    private String jobDescription;
    private String notes;
    private String contactPerson;
    private String contactEmail;
    private Instant createdAt;
    private Instant updatedAt;

    public JobApplicationResponse() {
    }

    public static JobApplicationResponse fromEntity(JobApplication entity) {
        if (entity == null) return null;
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(entity.getId());
        response.setCompanyName(entity.getCompanyName());
        response.setJobTitle(entity.getJobTitle());
        response.setJobLocation(entity.getJobLocation());
        response.setWorkMode(entity.getWorkMode());
        response.setEmploymentType(entity.getEmploymentType());
        response.setStatus(entity.getStatus());
        response.setSalaryCurrency(entity.getSalaryCurrency());
        response.setSalaryMin(entity.getSalaryMin());
        response.setSalaryMax(entity.getSalaryMax());
        response.setJobUrl(entity.getJobUrl());
        response.setApplicationDate(entity.getApplicationDate());
        response.setJobDescription(entity.getJobDescription());
        response.setNotes(entity.getNotes());
        response.setContactPerson(entity.getContactPerson());
        response.setContactEmail(entity.getContactEmail());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
