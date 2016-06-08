package com.example.mat.financialmanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mat.financialmanager.model.Invoice;
import com.example.mat.financialmanager.model.Tax;

import java.util.ArrayList;

/**
 * Created by mat on 08.06.16..
 */
public class SQLiteTax extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "finmgr";

    // Ime tablice oglasa
    private static final String TABLE_TAXES = "Taxes";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_DATE_DUE = "date_due";
    private static final String COLUMN_DATE_ISSUE= "date_issue";
    private static final String COLUMN_CURRENCY = "currency";
    private static final String COLUMN_INVOICE_NUMBER = "invoice_number";


    public SQLiteTax(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TAX_TABLE = "CREATE TABLE " + TABLE_TAXES + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_VALUE + " REAL,"
                + COLUMN_COMPANY + " TEXT,"
                + COLUMN_DATE_DUE + " TEXT,"
                + COLUMN_DATE_ISSUE + " TEXT,"
                + COLUMN_CURRENCY + " TEXT,"
                + COLUMN_INVOICE_NUMBER + " TEXT"
                + ")";
        db.execSQL(CREATE_TAX_TABLE);
    }

    public void updateTax(Tax tax) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, tax.getUserId());
        values.put(COLUMN_NAME, tax.getName());
        values.put(COLUMN_VALUE, tax.getValue());
        values.put(COLUMN_COMPANY, tax.getCompany());
        values.put(COLUMN_DATE_DUE, tax.getDateDue().toString());
        values.put(COLUMN_DATE_ISSUE, tax.getDateIssue().toString());
        values.put(COLUMN_CURRENCY, tax.getCurrency());
        values.put(COLUMN_INVOICE_NUMBER, tax.getInvoiceNumber());


        db.update(TABLE_TAXES, values, COLUMN_ID + "=\'" + tax.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertTax(Tax tax) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_TAXES + " WHERE " + COLUMN_ID + " = \'" +
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
        values.put(COLUMN_USER_ID, tax.getUserId());
        values.put(COLUMN_NAME, tax.getName());
        values.put(COLUMN_VALUE, tax.getValue());
        values.put(COLUMN_COMPANY, tax.getCompany());
        values.put(COLUMN_DATE_DUE, tax.getDateDue().toString());
        values.put(COLUMN_DATE_ISSUE, tax.getDateIssue().toString());
        values.put(COLUMN_CURRENCY, tax.getCurrency());
        values.put(COLUMN_INVOICE_NUMBER, tax.getInvoiceNumber());

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAXES);
        onCreate(db);
    }
}
