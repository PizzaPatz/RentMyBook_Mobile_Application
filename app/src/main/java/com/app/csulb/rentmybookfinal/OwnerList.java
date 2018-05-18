package com.app.csulb.rentmybookfinal;

import java.io.Serializable;

/*
    All the attributes of the owner of the book
 */

public class OwnerList implements Serializable {

    private String name, price, condition, available;

    public OwnerList(){}

    public OwnerList(String name, String price, String condition, String available) {
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}