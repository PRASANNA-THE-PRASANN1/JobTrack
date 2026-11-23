package com.jobtrack.dto;

import java.util.Map;

public class ApplicationStatsResponse {

    private long totalApplications;
    private long activeApplications;
    private long appliedCount;
    private long screeningCount;
    private long interviewingCount;
    private long offeredCount;
    private long rejectedCount;
    private long withdrawnCount;
    private long savedCount;
    private Map<String, Long> byStatus;

    public ApplicationStatsResponse() {
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getActiveApplications() {
        return activeApplications;
    }

    public void setActiveApplications(long activeApplications) {
        this.activeApplications = activeApplications;
    }

    public long getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(long appliedCount) {
        this.appliedCount = appliedCount;
    }

    public long getScreeningCount() {
        return screeningCount;
    }

    public void setScreeningCount(long screeningCount) {
        this.screeningCount = screeningCount;
    }

    public long getInterviewingCount() {
        return interviewingCount;
    }

    public void setInterviewingCount(long interviewingCount) {
        this.interviewingCount = interviewingCount;
    }

    public long getOfferedCount() {
        return offeredCount;
    }

    public void setOfferedCount(long offeredCount) {
        this.offeredCount = offeredCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getWithdrawnCount() {
        return withdrawnCount;
    }

    public void setWithdrawnCount(long withdrawnCount) {
        this.withdrawnCount = withdrawnCount;
    }

    public long getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(long savedCount) {
        this.savedCount = savedCount;
    }

    public Map<String, Long> getByStatus() {
        return byStatus;
    }

    public void setByStatus(Map<String, Long> byStatus) {
        this.byStatus = byStatus;
    }
}
