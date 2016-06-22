package com.example.mat.financialmanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.Fund;
import com.example.mat.financialmanager.model.Invoice;
import com.example.mat.financialmanager.model.PensionFund;
import com.example.mat.financialmanager.model.Share;
import com.example.mat.financialmanager.model.Tax;
import com.example.mat.financialmanager.model.TermSaving;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mat on 29.05.16..
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "finmgr";

    // Currencies
    private static final String TABLE_CURRENCIES = "Currencies";

    private static final String COLUMN_BASE = "BASE";
    private static final String COLUM_HRK = Currencies.HRK.toString();
    private static final String COLUM_EUR = Currencies.EUR.toString();
    private static final String COLUM_USD = Currencies.USD.toString();


    // Funds
    private static final String TABLE_FUNDS = "Funds";

    // Table columns
    private static final String COLUMN_FUND_ID = "id";
    private static final String COLUMN_FUND_USER_ID = "user_id";
    private static final String COLUMN_FUND_NAME = "name";
    private static final String COLUMN_FUND_MONTHLY_TAX = "monthly_tax";
    private static final String COLUMN_FUND_TYPE = "fund_type";
    private static final String COLUMN_FUND_DATE_DUE = "date_due";
    private static final String COLUMN_FUND_VALUE_AFTER = "value_after";
    private static final String COLUMN_FUND_INTEREST = "interest";
    private static final String COLUMN_FUND_VALUE = "value";
    private static final String COLUMN_FUND_CURRENCY = "currency";
    private static final String COLUMN_FUND_BANK = "bank";


    // Invoices
    private static final String TABLE_INVOICES = "Invoices";

    // Table columns
    private static final String COLUMN_INVOICE_ID = "id";
    private static final String COLUMN_INVOICE_USER_ID = "user_id";
    private static final String COLUMN_INVOICE_NAME = "name";
    private static final String COLUMN_INVOICE_INVOICE_NUMBER = "invoice_number";
    private static final String COLUMN_INVOICE_CARD_NUMBER = "card_number";
    private static final String COLUMN_INVOICE_CARD_EXPIRY = "card_expiry";
    private static final String COLUMN_INVOICE_CARD_TYPE = "card_type";
    private static final String COLUMN_INVOICE_BALANCE = "balance";
    private static final String COLUMN_INVOICE_CURRENCY = "currency";
    private static final String COLUMN_INVOICE_BANK = "bank";


    // Shares
    private static final String TABLE_SHARES = "Shares";

    // Table columns
    private static final String COLUMN_SHARE_ID = "id";
    private static final String COLUMN_SHARE_USER_ID = "user_id";
    private static final String COLUMN_SHARE_NAME = "name";
    private static final String COLUMN_SHARE_QUANTITY = "quantity";
    private static final String COLUMN_SHARE_COMPANY = "company";
    private static final String COLUMN_SHARE_VALUE = "value";
    private static final String COLUMN_SHARE_VALUE_PER_SHARE = "value_per_share";
    private static final String COLUMN_SHARE_DATE_BOUGHT = "date_bought";
    private static final String COLUMN_SHARE_CURRENCY = "currency";


    // Taxes
    private static final String TABLE_TAXES = "Taxes";

    // Table columns
    private static final String COLUMN_TAX_ID = "id";
    private static final String COLUMN_TAX_USER_ID = "user_id";
    private static final String COLUMN_TAX_NAME = "name";
    private static final String COLUMN_TAX_VALUE = "value";
    private static final String COLUMN_TAX_COMPANY = "company";
    private static final String COLUMN_TAX_DATE_DUE = "date_due";
    private static final String COLUMN_TAX_DATE_ISSUE= "date_issue";
    private static final String COLUMN_TAX_CURRENCY = "currency";
    private static final String COLUMN_TAX_INVOICE_NUMBER = "invoice_number";

    String resultString = new String();

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Invoice Table
        String CREATE_CURRENCIES_TABLE = "CREATE TABLE " + TABLE_CURRENCIES + "("
                + COLUMN_BASE + " TEXT PRIMARY KEY,"
                + COLUM_HRK + " REAL,"
                + COLUM_EUR + " REAL,"
                + COLUM_USD + " REAL"
                + ")";
        db.execSQL(CREATE_CURRENCIES_TABLE);

//      Default rates for HRK base
        ContentValues values = new ContentValues();
        values.put(COLUMN_BASE, Currencies.HRK.toString());
        values.put(COLUM_HRK, 1);
        values.put(COLUM_EUR, 0.1335);
        values.put(COLUM_USD, 0.1491);

        db.insert(TABLE_CURRENCIES, null, values);

//      Default rates for EUR base
        values = new ContentValues();
        values.put(COLUMN_BASE, Currencies.EUR.toString());
        values.put(COLUM_HRK, 7.4905);
        values.put(COLUM_EUR, 1);
        values.put(COLUM_USD, 1.1168);

        db.insert(TABLE_CURRENCIES, null, values);

//      Default rates for USD base
        values = new ContentValues();
        values.put(COLUMN_BASE, Currencies.USD.toString());
        values.put(COLUM_HRK, 6.7071);
        values.put(COLUM_EUR, 0.89542);
        values.put(COLUM_USD, 1);

        db.insert(TABLE_CURRENCIES, null, values);

        // Funds Table
        String CREATE_FUNDS_TABLE = "CREATE TABLE " + TABLE_FUNDS + "("
                + COLUMN_FUND_ID + " TEXT PRIMARY KEY,"
                + COLUMN_FUND_USER_ID + " TEXT,"
                + COLUMN_FUND_NAME + " TEXT,"
                + COLUMN_FUND_MONTHLY_TAX + " REAL,"
                + COLUMN_FUND_TYPE + " TEXT,"
                + COLUMN_FUND_DATE_DUE + " TEXT,"
                + COLUMN_FUND_VALUE_AFTER + " REAL,"
                + COLUMN_FUND_INTEREST + " REAL,"
                + COLUMN_FUND_VALUE + " REAL,"
                + COLUMN_FUND_CURRENCY + " TEXT,"
                + COLUMN_FUND_BANK + " TEXT"
                + ")";

        db.execSQL(CREATE_FUNDS_TABLE);

        String CREATE_INVOICE_TABLE = "CREATE TABLE " + TABLE_INVOICES + "("
                + COLUMN_INVOICE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_INVOICE_USER_ID + " TEXT,"
                + COLUMN_INVOICE_NAME + " TEXT,"
                + COLUMN_INVOICE_INVOICE_NUMBER + " TEXT,"
                + COLUMN_INVOICE_CARD_NUMBER + " TEXT,"
                + COLUMN_INVOICE_CARD_EXPIRY + " TEXT,"
                + COLUMN_INVOICE_CARD_TYPE + " TEXT,"
                + COLUMN_INVOICE_BALANCE + " REAL,"
                + COLUMN_INVOICE_CURRENCY + " TEXT,"
                + COLUMN_INVOICE_BANK + " TEXT"
                + ")";
        db.execSQL(CREATE_INVOICE_TABLE);

        String CREATE_SHARES_TABLE = "CREATE TABLE " + TABLE_SHARES + "("
                + COLUMN_SHARE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_SHARE_USER_ID + " TEXT,"
                + COLUMN_SHARE_NAME + " TEXT,"
                + COLUMN_SHARE_QUANTITY + " INTEGER,"
                + COLUMN_SHARE_COMPANY + " TEXT,"
                + COLUMN_SHARE_VALUE + " REAL,"
                + COLUMN_SHARE_VALUE_PER_SHARE + " REAL,"
                + COLUMN_SHARE_DATE_BOUGHT + " TEXT,"
                + COLUMN_SHARE_CURRENCY + " TEXT"
                + ")";

        db.execSQL(CREATE_SHARES_TABLE);

        String CREATE_TAX_TABLE = "CREATE TABLE " + TABLE_TAXES + "("
                + COLUMN_TAX_ID + " TEXT PRIMARY KEY,"
                + COLUMN_TAX_USER_ID + " TEXT,"
                + COLUMN_TAX_NAME + " TEXT,"
                + COLUMN_TAX_VALUE + " REAL,"
                + COLUMN_TAX_COMPANY + " TEXT,"
                + COLUMN_TAX_DATE_DUE + " TEXT,"
                + COLUMN_TAX_DATE_ISSUE + " TEXT,"
                + COLUMN_TAX_CURRENCY + " TEXT,"
                + COLUMN_TAX_INVOICE_NUMBER + " TEXT"
                + ")";
        db.execSQL(CREATE_TAX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUNDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHARES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAXES);

        onCreate(db);
    }

    public void updateTax(Tax tax) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TAX_USER_ID, tax.getUserId());
        values.put(COLUMN_TAX_NAME, tax.getName());
        values.put(COLUMN_TAX_VALUE, tax.getValue());
        values.put(COLUMN_TAX_COMPANY, tax.getCompany());
        values.put(COLUMN_TAX_DATE_DUE, tax.getDateDue().toString());
        values.put(COLUMN_TAX_DATE_ISSUE, tax.getDateIssue().toString());
        values.put(COLUMN_TAX_CURRENCY, tax.getCurrency());
        values.put(COLUMN_TAX_INVOICE_NUMBER, tax.getInvoiceNumber());


        db.update(TABLE_TAXES, values, COLUMN_TAX_ID + "=\'" + tax.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertTax(Tax tax) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_TAXES + " WHERE " + COLUMN_TAX_ID + " = \'" +
                tax.getId() + "\'";

        Cursor mCount = db.rawQuery(query, null);
        mCount.moveToFirst();
        int exists = mCount.getInt(0);

        if (exists == 0)
            addTax(tax);

        else
            updateTax(tax);

        db.close();
    }

    public void addTax(Tax tax) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TAX_USER_ID, tax.getUserId());
        values.put(COLUMN_TAX_NAME, tax.getName());
        values.put(COLUMN_TAX_VALUE, tax.getValue());
        values.put(COLUMN_TAX_COMPANY, tax.getCompany());
        values.put(COLUMN_TAX_DATE_DUE, tax.getDateDue().toString());
        values.put(COLUMN_TAX_DATE_ISSUE, tax.getDateIssue().toString());
        values.put(COLUMN_TAX_CURRENCY, tax.getCurrency());
        values.put(COLUMN_TAX_INVOICE_NUMBER, tax.getInvoiceNumber());

        long id = db.insert(TABLE_TAXES, null, values);
        db.close();
    }

    public ArrayList<Tax> getTaxes() {
        ArrayList<Tax> taxes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TAXES + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Tax tax = new Tax();

                        tax.setId(cursor.getString(0));
                        tax.setUserId(cursor.getString(1));
                        tax.setName(cursor.getString(2));
                        tax.setValue(Double.parseDouble(cursor.getString(3)));
                        tax.setCompany(cursor.getString(4));
                        tax.setDateDue(cursor.getString(5));
                        tax.setDateIssue(cursor.getString(6));
                        tax.setCurrency(cursor.getString(7));
                        tax.setInvoiceNumber(cursor.getString(8));


                        taxes.add(tax);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return taxes;
    }

    public void updateShare(Share share) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SHARE_USER_ID, share.getUserId());
        values.put(COLUMN_SHARE_NAME, share.getName());
        values.put(COLUMN_SHARE_QUANTITY, Integer.toString(share.getQuantity()));
        values.put(COLUMN_SHARE_COMPANY, share.getCompany());
        values.put(COLUMN_SHARE_VALUE, Double.toString(share.getValue()));
        values.put(COLUMN_SHARE_VALUE_PER_SHARE, Double.toString(share.getValuePerShare()));
        values.put(COLUMN_SHARE_DATE_BOUGHT, share.getDateBought().toString());
        values.put(COLUMN_SHARE_CURRENCY, share.getCurrency());

        db.update(TABLE_SHARES, values, COLUMN_SHARE_ID + "=\'" + share.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertShare(Share share) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_SHARES + " WHERE " + COLUMN_SHARE_ID + " = \'" +
                share.getId() + "\'";

        Cursor mCount = db.rawQuery(query, null);
        mCount.moveToFirst();
        int exists = mCount.getInt(0);

        if (exists == 0)
            addShare(share);

        else
            updateShare(share);

        db.close();
    }

    public void addShare(Share share) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SHARE_USER_ID, share.getUserId());
        values.put(COLUMN_SHARE_NAME, share.getName());
        values.put(COLUMN_SHARE_QUANTITY, Integer.toString(share.getQuantity()));
        values.put(COLUMN_SHARE_COMPANY, share.getCompany());
        values.put(COLUMN_SHARE_VALUE, Double.toString(share.getValue()));
        values.put(COLUMN_SHARE_VALUE_PER_SHARE, Double.toString(share.getValuePerShare()));
        values.put(COLUMN_SHARE_DATE_BOUGHT, share.getDateBought().toString());
        values.put(COLUMN_SHARE_CURRENCY, share.getCurrency());

        long id = db.insert(TABLE_SHARES, null, values);
        db.close();
    }

    public ArrayList<Share> getShares() {
        ArrayList<Share> shares = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SHARES + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Share share = new Share();

                        share.setId(cursor.getString(0));
                        share.setUserId(cursor.getString(1));
                        share.setName(cursor.getString(2));
                        share.setQuantity(cursor.getInt(3));
                        share.setCompany(cursor.getString(4));
                        share.setValue(Double.parseDouble(cursor.getString(5)));
                        share.setValuePerShare(Double.parseDouble(cursor.getString(6)));
                        share.setDateBought(Share.getDateFromString(cursor.getString(7)));
                        share.setCurrency(cursor.getString(8));

                        shares.add(share);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return shares;
    }

    public void updateInvoice(Invoice invoice) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INVOICE_USER_ID, invoice.getUserId());
        values.put(COLUMN_INVOICE_NAME, invoice.getName());
        values.put(COLUMN_INVOICE_INVOICE_NUMBER, invoice.getInvoiceNumber());
        values.put(COLUMN_INVOICE_CARD_NUMBER, invoice.getCardNumber());
        values.put(COLUMN_INVOICE_CARD_EXPIRY, invoice.getCardExpiry().toString());
        values.put(COLUMN_INVOICE_CARD_TYPE, invoice.getCardType());
        values.put(COLUMN_INVOICE_BALANCE, invoice.getBalance());
        values.put(COLUMN_INVOICE_CURRENCY, invoice.getCurrency());
        values.put(COLUMN_INVOICE_BANK, invoice.getBank());

        db.update(TABLE_INVOICES, values, COLUMN_INVOICE_ID + "=\'" + invoice.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertInvoice(Invoice invoice) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_INVOICES + " WHERE " + COLUMN_INVOICE_ID + " = \'" +
                invoice.getId() + "\'";

        Cursor mCount = db.rawQuery(query, null);
        mCount.moveToFirst();
        int exists = mCount.getInt(0);

        if (exists == 0)
            addInvoice(invoice);

        else
            updateInvoice(invoice);

        db.close();
    }

    public void addInvoice(Invoice invoice) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INVOICE_ID, invoice.getId());
        values.put(COLUMN_INVOICE_USER_ID, invoice.getUserId());
        values.put(COLUMN_INVOICE_NAME, invoice.getName());
        values.put(COLUMN_INVOICE_INVOICE_NUMBER, invoice.getInvoiceNumber());
        values.put(COLUMN_INVOICE_CARD_NUMBER, invoice.getCardNumber());
        values.put(COLUMN_INVOICE_CARD_EXPIRY, invoice.getCardExpiry().toString());
        values.put(COLUMN_INVOICE_CARD_TYPE, invoice.getCardType());
        values.put(COLUMN_INVOICE_BALANCE, invoice.getBalance());
        values.put(COLUMN_INVOICE_CURRENCY, invoice.getCurrency());
        values.put(COLUMN_INVOICE_BANK, invoice.getBank());

        long id = db.insert(TABLE_INVOICES, null, values);
        db.close();
    }

    public ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_INVOICES + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Invoice inv = new Invoice();

                        inv.setId(cursor.getString(0));
                        inv.setUserId(cursor.getString(1));
                        inv.setName(cursor.getString(2));
                        inv.setInvoiceNumber(cursor.getString(3));
                        inv.setCardNumber(cursor.getString(4));
                        inv.setCardExpiry(Invoice.getDateFromString(cursor.getString(5)));
                        inv.setCardType(cursor.getString(6));
                        inv.setBalance(cursor.getDouble(7));
                        inv.setCurrency(cursor.getString(8));
                        inv.setBank(cursor.getString(9));

                        invoices.add(inv);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return invoices;
    }

    public double getFromTo(String currFrom, String currTo, double value){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT " + currTo + " FROM " + TABLE_CURRENCIES + " WHERE " + COLUMN_BASE + " = \'" +
                currFrom + "\'";

        Cursor mCount = db.rawQuery(query, null);
        mCount.moveToFirst();
        double rate = mCount.getDouble(0);
        return value * rate;
    }

    public void updateTableRate(String base, Map<String, Double> rateMap) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUM_HRK, rateMap.get(Currencies.HRK.toString()));
        values.put(COLUM_EUR, rateMap.get(Currencies.EUR.toString()));
        values.put(COLUM_USD, rateMap.get(Currencies.USD.toString()));

        db.update(TABLE_CURRENCIES, values, COLUMN_BASE + "=\'" + base + "\'", null);
        db.close();
    }

    public void updateFund(Fund fund) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FUND_USER_ID, fund.getUserId());
        values.put(COLUMN_FUND_NAME, fund.getName());
        values.put(COLUMN_FUND_DATE_DUE, fund.getDateDue().toString());

        if(fund.getFundType().equals(FundTypes.PENSION_FUND))
            values.put(COLUMN_FUND_MONTHLY_TAX, ((PensionFund)fund).getMonthlyTax());

        else if(fund.getFundType().equals(FundTypes.TERM_SAVING)){
            values.put(COLUMN_FUND_VALUE_AFTER, Double.toString(((TermSaving)fund).getValueAfter()));
            values.put(COLUMN_FUND_INTEREST, Double.toString(((TermSaving)fund).getInterest()));
        }
        values.put(COLUMN_FUND_TYPE, fund.getFundType());
        values.put(COLUMN_FUND_VALUE, Double.toString(fund.getValue()));
        values.put(COLUMN_FUND_CURRENCY, fund.getCurrency());
        values.put(COLUMN_FUND_BANK, fund.getBank());

        db.update(TABLE_FUNDS, values, COLUMN_FUND_ID + "=\'" + fund.getId() + "\'", null);
        db.close();
    }
    public void updateOrInsertFund(Fund fund) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_FUNDS + " WHERE " + COLUMN_FUND_ID + " = \'" +
                fund.getId() + "\'";

        Cursor mCount = db.rawQuery(query, null);
        mCount.moveToFirst();
        int exists = mCount.getInt(0);

        if (exists == 0)
            addFund(fund);

        else
            updateFund(fund);

        db.close();
    }

    public void addFund(Fund fund) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FUND_USER_ID, fund.getUserId());
        values.put(COLUMN_FUND_NAME, fund.getName());
        values.put(COLUMN_FUND_DATE_DUE, fund.getDateDue().toString());

        if(fund.getFundType().equals(FundTypes.PENSION_FUND))
            values.put(COLUMN_FUND_MONTHLY_TAX, ((PensionFund)fund).getMonthlyTax());

        else if(fund.getFundType().equals(FundTypes.TERM_SAVING)){
            values.put(COLUMN_FUND_VALUE_AFTER, Double.toString(((TermSaving)fund).getValueAfter()));
            values.put(COLUMN_FUND_INTEREST, Double.toString(((TermSaving)fund).getInterest()));
        }
        values.put(COLUMN_FUND_TYPE, fund.getFundType());
        values.put(COLUMN_FUND_VALUE, Double.toString(fund.getValue()));
        values.put(COLUMN_FUND_CURRENCY, fund.getCurrency());
        values.put(COLUMN_FUND_BANK, fund.getBank());

        long id = db.insert(TABLE_FUNDS, null, values);
        db.close();
    }

    public ArrayList<Fund> getFunds() {
        ArrayList<Fund> funds = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FUNDS + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Fund fund = new Fund();

                        fund.setId(cursor.getString(0));
                        fund.setUserId(cursor.getString(1));
                        fund.setName(cursor.getString(2));

                        if (cursor.getString(cursor.getColumnIndex("fund_type")).equals(FundTypes.PENSION_FUND))
                            ((PensionFund)fund).setMonthlyTax(cursor.getDouble(3));

                        else if (cursor.getString(cursor.getColumnIndex("fund_type")).equals(FundTypes.TERM_SAVING)) {
                            ((TermSaving)fund).setValueAfter(cursor.getDouble(6));
                            ((TermSaving)fund).setInterest(cursor.getDouble(7));
                        }

                        fund.setFundType(cursor.getString(4));
                        fund.setDateDue(Fund.getDateFromString(cursor.getString(5)));

                        fund.setValue(cursor.getDouble(8));
                        fund.setCurrency(cursor.getString(9));
                        fund.setBank(cursor.getString(10));

                        funds.add(fund);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return funds;
    }


    public void updateTableCurrencies(Context context){
        UpdateRatesAsyncTask ratesTask = new UpdateRatesAsyncTask(context, AppConfig.CURRENCY_API_BASE_HRK);
        ratesTask.execute();
        if (!resultString.isEmpty()){
            try{
                JSONObject jObj = new JSONObject(resultString);
                JSONObject jRatesBaseHrk = jObj.getJSONObject("rates");

                ArrayList<String> currencies = Currencies.getNames();

                for (String currencyBase : currencies) {
                    Map<String, Double> rateMap = new HashMap<>();
                    for (String currencyRate : currencies) {
                        if (!currencyBase.equals(currencyRate) && currencyBase.equals(Currencies.HRK.toString()))
                            rateMap.put(currencyRate, jRatesBaseHrk.getDouble(currencyRate));
                        else if (!currencyBase.equals(currencyRate)){
                            double rate = 1/jRatesBaseHrk.getDouble(currencyBase);
                            if(currencyRate.equals(Currencies.HRK.toString()))
                                rateMap.put(currencyRate, rate);
                            else
                                rateMap.put(currencyRate, rate * jRatesBaseHrk.getDouble(currencyRate));
                        }
                    }
                    rateMap.put(currencyBase, 1.0);
                    updateTableRate(currencyBase, rateMap);
                }

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class UpdateRatesAsyncTask extends AsyncTask<Void, Void, String> {
        private Context mContext;
        private String mUrl;

        public UpdateRatesAsyncTask(Context context, String url) {
            mContext = context;
            mUrl = url;
        }


        public String getJSON(String url) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected String doInBackground(Void... params) {
            resultString = getJSON(mUrl);
            return resultString;
        }
    }


}
