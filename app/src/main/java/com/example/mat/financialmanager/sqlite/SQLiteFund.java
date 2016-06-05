package com.example.mat.financialmanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.Fund;
import com.example.mat.financialmanager.model.PensionFund;
import com.example.mat.financialmanager.model.TermSaving;

import java.util.ArrayList;

/**
 * Created by mat on 28.05.16..
 */
public class SQLiteFund extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "finmgr";

    // Ime tablice oglasa
    private static final String TABLE_FUNDS = "Funds";


    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MONTHLY_TAX = "monthly_tax";
    private static final String COLUMN_FUND_TYPE = "fund_type";
    private static final String COLUMN_DATE_DUE = "date_due";
    private static final String COLUMN_VALUE_AFTER = "value_after";
    private static final String COLUMN_INTEREST = "interest";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_CURRENCY = "currency";
    private static final String COLUMN_BANK = "bank";

    public SQLiteFund(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FUNDS_TABLE = "CREATE TABLE " + TABLE_FUNDS + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_MONTHLY_TAX + " REAL,"
                + COLUMN_FUND_TYPE + " TEXT,"
                + COLUMN_DATE_DUE + " TEXT,"
                + COLUMN_VALUE_AFTER + " REAL,"
                + COLUMN_INTEREST + " REAL,"
                + COLUMN_VALUE + " REAL,"
                + COLUMN_CURRENCY + " TEXT,"
                + COLUMN_BANK + " TEXT"
                + ")";

        db.execSQL(CREATE_FUNDS_TABLE);
    }

    public void updateFund(Fund fund) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, fund.getUserId());
        values.put(COLUMN_NAME, fund.getName());
        values.put(COLUMN_DATE_DUE, fund.getDateDue().toString());

        if(fund.getFundType().equals(FundTypes.PENSION_FUND))
            values.put(COLUMN_MONTHLY_TAX, ((PensionFund)fund).getMonthlyTax());

        else if(fund.getFundType().equals(FundTypes.TERM_SAVING)){
            values.put(COLUMN_VALUE_AFTER, Double.toString(((TermSaving)fund).getValueAfter()));
            values.put(COLUMN_INTEREST, Double.toString(((TermSaving)fund).getInterest()));
        }
        values.put(COLUMN_FUND_TYPE, fund.getFundType());
        values.put(COLUMN_VALUE, Double.toString(fund.getValue()));
        values.put(COLUMN_CURRENCY, fund.getCurrency());
        values.put(COLUMN_BANK, fund.getBank());

        db.update(TABLE_FUNDS, values, COLUMN_ID + "=\'" + fund.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertFund(Fund fund) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_FUNDS + " WHERE " + COLUMN_ID + " = \'" +
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
        values.put(COLUMN_USER_ID, fund.getUserId());
        values.put(COLUMN_NAME, fund.getName());
        values.put(COLUMN_DATE_DUE, fund.getDateDue().toString());

        if(fund.getFundType().equals(FundTypes.PENSION_FUND))
            values.put(COLUMN_MONTHLY_TAX, ((PensionFund)fund).getMonthlyTax());

        else if(fund.getFundType().equals(FundTypes.TERM_SAVING)){
            values.put(COLUMN_INTEREST, Double.toString(((TermSaving)fund).getInterest()));
            values.put(COLUMN_VALUE_AFTER, Double.toString(((TermSaving)fund).getValueAfter()));
        }

        values.put(COLUMN_FUND_TYPE, fund.getFundType());
        values.put(COLUMN_VALUE, Double.toString(fund.getValue()));
        values.put(COLUMN_CURRENCY, fund.getCurrency());
        values.put(COLUMN_BANK, fund.getBank());

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUNDS);
        onCreate(db);
    }
}
