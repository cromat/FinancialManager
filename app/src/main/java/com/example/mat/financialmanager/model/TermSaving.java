package com.example.mat.financialmanager.model;

import java.util.Date;

/**
 * Created by mat on 22.05.16..
 */
public class TermSaving extends Fund {

    private double interest;
    private double valueAfter;

    public TermSaving(){}


    public TermSaving(String id, String userId, String name, double value, String bank,
                      Date dateDue, double interest, double valueAfter, String currency, String fundType) {
        super(id, userId,name, value, bank, dateDue, currency, fundType);
        this.interest = interest;
        this.valueAfter = valueAfter;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getValueAfter() {
        return valueAfter;
    }

    public void setValueAfter(double valueAfter) {
        this.valueAfter = valueAfter;
    }
}
