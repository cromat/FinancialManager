package com.example.mat.financialmanager.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mat on 08.06.16..
 */
public class Tax implements Serializable{
    private String id;
    private String userId;
    private String name;
    private double value;
    private String company;
    private Date dateDue;
    private Date dateIssue;
    private String currency;
    private String invoiceNumber;

    public Tax(){}

    public Tax(String id, String userId, String name, double value, String company, Date dateDue,
               Date dateIssue, String currency, String invoiceNumber) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.value = value;
        this.company = company;
        this.dateDue = dateDue;
        this.dateIssue = dateIssue;
        this.currency = currency;
        this.invoiceNumber = invoiceNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public String getCroDateDue(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateDue);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year) + ".";
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public void setDateDue(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US );
        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.dateDue = date;
    }

    public Date getDateIssue() {
        return dateIssue;
    }

    public String getCroDateIssue(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateIssue);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year) + ".";
    }

    public void setDateIssue(Date dateIssue) {
        this.dateIssue = dateIssue;
    }

    public void setDateIssue(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US );
        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.dateDue = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
