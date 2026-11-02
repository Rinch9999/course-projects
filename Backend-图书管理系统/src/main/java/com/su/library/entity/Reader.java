package com.su.library.entity;

import java.time.LocalDateTime;

/**
 * 读者实体，对应表 reader。
 */
public class Reader {
    private int id;
    private String name;
    private String phone;
    private LocalDateTime createdAt;

    public Reader() {}

    public Reader(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Reader{id=" + id + ", name='" + name + "', phone='" + phone + "'}";
    }
}
