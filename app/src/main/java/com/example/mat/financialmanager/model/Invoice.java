package com.example.mat.financialmanager.model;

import java.util.Date;

/**
 * Created by mat on 03.04.16..
 */
public class Invoice {
    private String name;
    private String invoiceNumber;
    private String cardNumber;
    private Date cardExpiry;
    private String cardType;
    private double balance;

    public Invoice(String name, String invoiceNumber, String cardNumber,
                   Date cardExpiry, String cardType, double balance) {
        this.name = name;
        this.invoiceNumber = invoiceNumber;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardType = cardType;
        this.balance = balance;
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
}
