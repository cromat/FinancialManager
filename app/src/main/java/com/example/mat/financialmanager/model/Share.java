package com.example.mat.financialmanager.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mat on 22.05.16..
 */
public class Share implements Serializable{

    private String id;
    private String userId;
    private String name;
    private double value;
    private double valuePerShare;
    private int quantity;
    private  String company;
    private Date dateBought;
    private String currency;

    public Share(){}

    public Share(String id, String userId, String name, int quantity, String company, double value, double valuePerShare, Date dateBought, String currency) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.quantity = quantity;
        this.company = company;
        this.value = value;
        this.valuePerShare = valuePerShare;
        this.dateBought = dateBought;
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

    public void setDateBought(String dateBought) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = formatter.parse(dateBought);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.dateBought = date;
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
        cal.setTime(dateBought);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year) + ".";
    }

    public static Date getDateFromString(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US );
        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
