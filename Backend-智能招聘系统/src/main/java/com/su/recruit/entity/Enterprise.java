package com.su.recruit.entity;

/**
 * 企业实体，对应表 enterprise。status 用于企业审核流程。
 */
public class Enterprise {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    private int id;
    private int userId;
    private String name;
    private String industry;
    private String status;

    public Enterprise() {}

    public Enterprise(int userId, String name, String industry, String status) {
        this.userId = userId;
        this.name = name;
        this.industry = industry;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}