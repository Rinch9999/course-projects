package com.su.property.entity;

/**
 * 账号实体，对应表 user。角色：OWNER（业主）/ ADMIN（管理员）。
 */
public class User {
    public static final String ROLE_OWNER = "OWNER";
    public static final String ROLE_ADMIN = "ADMIN";

    private int id;
    private String username;
    private String password;
    private String role;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}