package com.example.mat.financialmanager.enums;

/**
 * Created by mat on 01.06.16..
 */
public enum FundTypes {
    PENSION_FUND("Pension Fund"),
    MUTUAL_FUND("Mutual Fund"),
    TERM_SAVING("Term Saving"),
    ;

    private final String text;

    private FundTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString () {
        return text;
    }
}