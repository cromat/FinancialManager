package com.example.mat.financialmanager.model;

import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class PensionFund extends Fund{

    private double monthlyTax;

    public PensionFund(){}

    public PensionFund(String id, String name, double value, String bank, Date dateDue, double monthlyTax, String currency) {
        super(id, name, value, bank, dateDue, currency);
        this.monthlyTax = monthlyTax;
    }

    public double getMonthlyTax() {
        return monthlyTax;
    }

    public void setMonthlyTax(double monthlyTax) {
        this.monthlyTax = monthlyTax;
    }
}
