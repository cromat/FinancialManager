package com.example.mat.financialmanager.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.CardTypes;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mat on 03.04.16..
 */
public class Invoice implements Serializable {
    private String id;
    private String name;
    private String invoiceNumber;
    private String cardNumber;
    private Date cardExpiry;
    private String cardType;
    private double balance;
    private String currency;
    private String bank;

    public Invoice(){
        this.id = null;
    }

    public Invoice(String id, String name, String invoiceNumber, String cardNumber,
                   Date cardExpiry, String cardType, double balance, String currency, String bank) {
        this.id = id;
        this.name = name;
        this.invoiceNumber = invoiceNumber;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardType = cardType;
        this.balance = balance;
        this.currency = currency;
        this.bank = bank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getCardExpiry() {
        return cardExpiry;
    }

    public String getCardExpiryYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(cardExpiry.toString()));
            return Integer.toString(c.get(Calendar.YEAR));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCardExpiryMonth() {
        int month = cardExpiry.getMonth();

        switch (month){
            case (0):
                return "Jan";
            case (1):
                return "Feb";
            case (2):
                return "Mar";
            case (3):
                return "Apr";
            case (4):
                return "May";
            case (5):
                return "Jun";
            case (6):
                return "Jul";
            case (7):
                return "Aug";
            case (8):
                return "Sep";
            case (9):
                return "Oct";
            case (10):
                return "Nov";
            case (11):
                return "Dec";
        }
        return null;
    }

    public static Date getDateFromString(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US );
        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setCardExpiry(Date cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = formatter.parse(cardExpiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.cardExpiry = date;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public static Drawable getCardImage(String cardType, Context context){

        if (cardType.equals(CardTypes.VISA.toString()))
            return ContextCompat.getDrawable(context, R.drawable.visa);
        else if (cardType.equals(CardTypes.MASTER_CARD.toString()))
            return ContextCompat.getDrawable(context, R.drawable.mastercard);
        else if (cardType.equals(CardTypes.MAESTRO.toString()))
            return ContextCompat.getDrawable(context, R.drawable.maestro);
        else if (cardType.equals(CardTypes.AMERICAN_EXPRESS.toString()))
            return ContextCompat.getDrawable(context, R.drawable.american);
        else if (cardType.equals(CardTypes.DISCOVER.toString()))
            return ContextCompat.getDrawable(context, R.drawable.discover);

        return ContextCompat.getDrawable(context, R.drawable.other);
    }

    public Drawable getCardImage(Context context){

        if (cardType.equals(CardTypes.VISA.toString()))
            return ContextCompat.getDrawable(context, R.drawable.visa);
        else if (cardType.equals(CardTypes.MASTER_CARD.toString()))
            return ContextCompat.getDrawable(context, R.drawable.mastercard);
        else if (cardType.equals(CardTypes.MAESTRO.toString()))
            return ContextCompat.getDrawable(context, R.drawable.maestro);
        else if (cardType.equals(CardTypes.AMERICAN_EXPRESS.toString()))
            return ContextCompat.getDrawable(context, R.drawable.american);
        else if (cardType.equals(CardTypes.DISCOVER.toString()))
            return ContextCompat.getDrawable(context, R.drawable.discover);

        return ContextCompat.getDrawable(context, R.drawable.other);
    }
}
