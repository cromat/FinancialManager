package com.example.mat.financialmanager.model;

import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class Share {

    private String id;
    private String name;
    private int quantity;
    private  String company;
    private double value;
    private double valuePerShare;
    private Date dateBought;

    public Share(){}

    public Share(String id, String name, int quantity, String company, double value, double valuePerShare, Date dateBought) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.company = company;
        this.value = value;
        this.valuePerShare = valuePerShare;
        this.dateBought = dateBought;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValuePerShare() {
        return valuePerShare;
    }

    public void setValuePerShare(double valuePerShare) {
        this.valuePerShare = valuePerShare;
    }

    public Date getDateBought() {
        return dateBought;
    }

    public void setDateBought(Date dateBought) {
        this.dateBought = dateBought;
    }
}
