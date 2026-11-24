package com.campusverse.municipal.dto;

import java.util.Map;

public class MunicipalStatisticsDto {
    private long totalRequests;
    private long pendingCount;
    private long inReviewCount;
    private long missingDocumentsCount;
    private long approvedCount;
    private long rejectedCount;
    private long completedCount;
    private double approvalRate;
    private double averageProcessingTimeHours;
    private Map<String, Long> requestsByService;
    private Map<String, Long> requestsByStatus;

    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    public long getPendingCount() { return pendingCount; }
    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }
    public long getInReviewCount() { return inReviewCount; }
    public void setInReviewCount(long inReviewCount) { this.inReviewCount = inReviewCount; }
    public long getMissingDocumentsCount() { return missingDocumentsCount; }
    public void setMissingDocumentsCount(long missingDocumentsCount) { this.missingDocumentsCount = missingDocumentsCount; }
    public long getApprovedCount() { return approvedCount; }
    public void setApprovedCount(long approvedCount) { this.approvedCount = approvedCount; }
    public long getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }
    public long getCompletedCount() { return completedCount; }
    public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }
    public double getApprovalRate() { return approvalRate; }
    public void setApprovalRate(double approvalRate) { this.approvalRate = approvalRate; }
    public double getAverageProcessingTimeHours() { return averageProcessingTimeHours; }
    public void setAverageProcessingTimeHours(double averageProcessingTimeHours) { this.averageProcessingTimeHours = averageProcessingTimeHours; }
    public Map<String, Long> getRequestsByService() { return requestsByService; }
    public void setRequestsByService(Map<String, Long> requestsByService) { this.requestsByService = requestsByService; }
    public Map<String, Long> getRequestsByStatus() { return requestsByStatus; }
    public void setRequestsByStatus(Map<String, Long> requestsByStatus) { this.requestsByStatus = requestsByStatus; }
}
