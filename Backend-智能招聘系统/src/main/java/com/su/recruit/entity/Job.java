package com.su.recruit.entity;

/**
 * 岗位实体，对应表 job。
 */
public class Job {
    private int id;
    private int enterpriseId;
    private Integer categoryId;
    private String title;
    private String description;
    private String status;

    public Job() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(int enterpriseId) { this.enterpriseId = enterpriseId; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
