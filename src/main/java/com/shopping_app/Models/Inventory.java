package com.shopping_app.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int InventoryId;

    private int ordered;
    private double price;
    private int available;
    public Inventory() {
    }

    public Inventory(int ordered, double price, int available) {
        this.ordered = ordered;
        this.price = price;
        this.available = available;
    }

    public int getId() {
        return this.InventoryId;
    }

    public int getOrdered() {
        return ordered;
    }
    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getAvailable() {
        return available;
    }
    public void setAvailable(int available) {
        this.available = available;
    }

}
