package com.example.mat.financialmanager.enums;

import java.util.ArrayList;

/**
 * Created by mat on 21.05.16..
 */
public enum Months{
    JAN(1, "Jan"),
    FEB(2, "Feb"),
    MAR(3, "Mar"),
    APR(4, "Apr"),
    MAY(5, "May"),
    JUN(6, "Jun"),
    JUL(7, "Jul"),
    AUG(8, "Aug"),
    SEP(9, "Sep"),
    OCT(10, "Oct"),
    NOV(11, "Nov"),
    DEC(12, "Dec"),
    ;

    private final int month;
    private final String text;

    private Months(final int month, final String text) {
        this.month = month;
        this.text = text;
    }

    public int getIntMonth(String month){
        switch (month){
            case ("Jan"):
                return 1;
            case ("Feb"):
                return 2;
            case ("Mar"):
                return 3;
            case ("Apr"):
                return 4;
            case ("May"):
                return 5;
            case ("Jun"):
                return 6;
            case ("Jul"):
                return 7;
            case ("Aug"):
                return 8;
            case ("Sep"):
                return 9;
            case ("Oct"):
                return 10;
            case ("Nov"):
                return 11;
            case ("Dec"):
                return 12;
        }
        return 0;
    }

    @Override
    public String toString () {
        return Integer.toString(month);
    }

    public static ArrayList<String> getNames(){
        Months[] months = values();

        ArrayList<String> monthNames = new ArrayList<String>();

        for (int i = 0; i < months.length; i++) {
            monthNames.add(months[i].text);
        }

        return monthNames;
    }
}
