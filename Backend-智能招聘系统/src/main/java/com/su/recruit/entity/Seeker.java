package com.su.recruit.entity;

/**
 * 求职者实体，对应表 job_seeker。
 */
public class Seeker {
    private int id;
    private int userId;
    private String name;
    private String phone;
    private String education;

    public Seeker() {}

    public Seeker(int userId, String name, String phone, String education) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.education = education;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
}