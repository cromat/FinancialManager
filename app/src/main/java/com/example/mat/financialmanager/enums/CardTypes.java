package com.example.mat.financialmanager.enums;

import java.util.ArrayList;

/**
 * Created by mat on 09.04.16..
 */
public enum CardTypes {
    VISA("VISA"),
    MASTER_CARD("Master Card"),
    AMERICAN_EXPRESS("American Express"),
    MAESTRO("Maestro"),
    DISCOVER("Discover"),
    OTHER("Other")
    ;

    private final String text;

    private CardTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString () {
        return text;
    }

    public static ArrayList<String> getNames(){
        CardTypes[] cardTypes = values();

        ArrayList<String> cardTypeNames = new ArrayList<String>();

        for (int i = 0; i < cardTypes.length; i++) {
            cardTypeNames.add(cardTypes[i].text);
        }

        return  cardTypeNames;
    }
}