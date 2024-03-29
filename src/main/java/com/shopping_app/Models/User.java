package com.shopping_app.Models;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int Id;
    private List<String> CouponsUsed= new ArrayList<>();
    private List<Integer> OrderIds= new ArrayList<>();
    public User(){}

    public User(int id, List<String> couponsUsed) {
        Id = id;
        CouponsUsed = couponsUsed;
    }

    public List<Integer> getOrderIds() {
        return OrderIds;
    }

    public void setOrderIds(List<Integer> orderIds) {
        OrderIds = orderIds;
    }

    public int getId() {
        return Id;
    }
    public List<String> getCouponsUsed() {
        return CouponsUsed;
    }
    public void setCouponsUsed(List<String> couponsUsed) {
        CouponsUsed = couponsUsed;
    }

}
