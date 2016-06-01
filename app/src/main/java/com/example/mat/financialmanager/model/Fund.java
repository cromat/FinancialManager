package com.example.mat.financialmanager.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class Fund implements Serializable{

    private String id;
    private String userId;
    private String name;
    private double value;
    private String bank;
    private Date dateDue;
    private String currency;
    private String fundType;

    public Fund(){}

    public Fund(String id, String userId, String name, double value, String bank, Date dateDue, String currency, String fundType) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.value = value;
        this.bank = bank;
        this.dateDue = dateDue;
        this.currency = currency;
        this.fundType = fundType;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStringCroDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateDue);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year) + ".";
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }


}
