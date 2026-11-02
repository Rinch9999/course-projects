package com.su.recruit.entity;

import java.time.LocalDateTime;

/**
 * 简历实体，对应表 resume。每个求职者一份在线简历。
 */
public class Resume {
    private int id;
    private int seekerId;
    private String content;
    private LocalDateTime updatedAt;

    public Resume() {}

    public Resume(int seekerId, String content) {
        this.seekerId = seekerId;
        this.content = content;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}