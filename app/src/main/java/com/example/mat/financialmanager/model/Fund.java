package com.example.mat.financialmanager.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class Fund implements Serializable{

    private String id;
    private String name;
    private double value;
    private String bank;
    private Date dateDue;
    private String currency;

    public Fund(){}

    public Fund(String id, String name, double value, String bank, Date dateDue, String currency) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.bank = bank;
        this.dateDue = dateDue;
        this.currency = currency;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
