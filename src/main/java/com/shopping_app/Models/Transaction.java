package com.shopping_app.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transaction {
    @Id
    private String transactionId;
    private int orderId ;
    private double amount;
    private LocalDateTime date;
    private String coupon;
    private String status;
    
    public Transaction() {
    }
    public Transaction(String transactionId, LocalDateTime date, String status,Order order) {
        this.transactionId = transactionId;
        this.date = date;
        this.status = status;
        this.orderId = order.getOrderId();
        this.amount = order.getAmount();
        this.coupon = order.getCoupon();
    }

    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getCoupon() {
        return coupon;
    }
    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
