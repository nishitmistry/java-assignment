package com.shopping_app.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
public class Coupon {

    @Id
    @Column
    private String CouponCode;
    @Column
    private double Discount;

    public Coupon() {
    }
    
    public Coupon(String couponCode, double discount) {
        CouponCode = couponCode;
        Discount = discount;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    
    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }
}
