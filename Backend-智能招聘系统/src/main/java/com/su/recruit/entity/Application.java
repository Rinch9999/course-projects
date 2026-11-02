package com.su.recruit.entity;

import java.time.LocalDateTime;

/**
 * 投递记录实体，对应表 application。
 */
public class Application {
    private int id;
    private int jobId;
    private int seekerId;
    private Integer resumeId;
    private String status;
    private LocalDateTime createdAt;

    public Application() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }
    public Integer getResumeId() { return resumeId; }
    public void setResumeId(Integer resumeId) { this.resumeId = resumeId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
