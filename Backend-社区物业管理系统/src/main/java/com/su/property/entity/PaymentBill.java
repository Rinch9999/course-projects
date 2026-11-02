package com.su.property.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费账单实体，对应表 payment_bill。
 */
public class PaymentBill {
    public static final String STATUS_UNPAID = "UNPAID";
    public static final String STATUS_PAID = "PAID";

    private int id;
    private int ownerId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}