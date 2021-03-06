package com.example.mat.financialmanager.enums;

import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<String> getNames(){
        Currencies[] currencies = values();

        ArrayList<String> currencyNames = new ArrayList<String>();

        for (int i = 0; i < currencies.length; i++) {
            currencyNames.add(currencies[i].text);
        }

        return  currencyNames;
    }
}
