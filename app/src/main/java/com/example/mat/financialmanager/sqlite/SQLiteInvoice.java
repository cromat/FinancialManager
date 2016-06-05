package com.example.mat.financialmanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mat.financialmanager.model.Invoice;

import java.util.ArrayList;

/**
 * Created by mat on 28.05.16..
 */
public class SQLiteInvoice extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "finmgr";

    // Ime tablice oglasa
    private static final String TABLE_INVOICES = "Invoices";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_INVOICE_NUMBER = "invoice_number";
    private static final String COLUMN_CARD_NUMBER = "card_number";
    private static final String COLUMN_CARD_EXPIRY = "card_expiry";
    private static final String COLUMN_CARD_TYPE = "card_type";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_CURRENCY = "currency";
    private static final String COLUMN_BANK = "bank";

    public SQLiteInvoice(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVOICE_TABLE = "CREATE TABLE " + TABLE_INVOICES + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_INVOICE_NUMBER + " TEXT,"
                + COLUMN_CARD_NUMBER + " TEXT,"
                + COLUMN_CARD_EXPIRY + " TEXT,"
                + COLUMN_CARD_TYPE + " TEXT,"
                + COLUMN_BALANCE + " REAL,"
                + COLUMN_CURRENCY + " TEXT,"
                + COLUMN_BANK + " TEXT"
                + ")";
        db.execSQL(CREATE_INVOICE_TABLE);
    }

    public void updateInvoice(Invoice invoice) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, invoice.getUserId());
        values.put(COLUMN_NAME, invoice.getName());
        values.put(COLUMN_INVOICE_NUMBER, invoice.getInvoiceNumber());
        values.put(COLUMN_CARD_NUMBER, invoice.getCardNumber());
        values.put(COLUMN_CARD_EXPIRY, invoice.getCardExpiry().toString());
        values.put(COLUMN_CARD_TYPE, invoice.getCardType());
        values.put(COLUMN_BALANCE, invoice.getBalance());
        values.put(COLUMN_CURRENCY, invoice.getCurrency());
        values.put(COLUMN_BANK, invoice.getBank());

        db.update(TABLE_INVOICES, values, COLUMN_ID + "=\'" + invoice.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertInvoice(Invoice invoice) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_INVOICES + " WHERE " + COLUMN_ID + " = \'" +
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
        values.put(COLUMN_ID, invoice.getId());
        values.put(COLUMN_USER_ID, invoice.getUserId());
        values.put(COLUMN_NAME, invoice.getName());
        values.put(COLUMN_INVOICE_NUMBER, invoice.getInvoiceNumber());
        values.put(COLUMN_CARD_NUMBER, invoice.getCardNumber());
        values.put(COLUMN_CARD_EXPIRY, invoice.getCardExpiry().toString());
        values.put(COLUMN_CARD_TYPE, invoice.getCardType());
        values.put(COLUMN_BALANCE, invoice.getBalance());
        values.put(COLUMN_CURRENCY, invoice.getCurrency());
        values.put(COLUMN_BANK, invoice.getBank());

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        onCreate(db);
    }
}
