package com.example.mat.financialmanager.enums;

/**
 * Created by mat on 21.05.16..
 */
public enum Currencies {
    HRK("HRK"),
    USD("USD"),
    EUR("EUR"),
    ;

    private final String text;

    private Currencies(final String text) {
        this.text = text;
    }

    @Override
    public String toString () {
        return text;
    }
}
