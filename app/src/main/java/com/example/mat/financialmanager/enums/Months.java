package com.example.mat.financialmanager.enums;

/**
 * Created by mat on 21.05.16..
 */
public enum Months{
    JAN(1),
    FEB(2),
    MAR(3),
    APR(4),
    MAY(5),
    JUN(6),
    JUL(7),
    AUG(8),
    SEP(9),
    OCT(10),
    NOV(11),
    DEC(12),
    ;

    private final int month;

    private Months(final int month) {
        this.month = month;
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
}
