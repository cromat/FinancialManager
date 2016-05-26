package com.example.mat.financialmanager.enums;

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
}