package com.example.mat.financialmanager.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mat on 03.04.16..
 */
public class Invoice {
    private String id;
    private String name;
    private String invoiceNumber;
    private String cardNumber;
    private Date cardExpiry;
    private String cardType;
    private double balance;
    private String currency;

    public Invoice(){}

    public Invoice(String id, String name, String invoiceNumber, String cardNumber,
                   Date cardExpiry, String cardType, double balance, String currency) {
        this.id = id;
        this.name = name;
        this.invoiceNumber = invoiceNumber;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardType = cardType;
        this.balance = balance;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(Date cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = formatter.parse(cardExpiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.cardExpiry = date;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
