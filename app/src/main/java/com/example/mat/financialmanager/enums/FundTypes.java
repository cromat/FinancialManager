package com.example.mat.financialmanager.enums;

import java.util.ArrayList;

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

    public static ArrayList<String> getNames(){
        FundTypes[] fundTypes = values();

        ArrayList<String> fundTypeNames = new ArrayList<String>();

        for (int i = 0; i < fundTypes.length; i++) {
            fundTypeNames.add(fundTypes[i].text);
        }

        return  fundTypeNames;
    }
}