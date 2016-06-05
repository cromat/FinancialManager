package com.example.mat.financialmanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mat.financialmanager.model.Share;

import java.util.ArrayList;

/**
 * Created by mat on 04.06.16..
 */
public class SQLiteShare extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "finmgr";

    // Ime tablice oglasa
    private static final String TABLE_SHARES = "Shares";

    // Table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_VALUE_PER_SHARE = "value_per_share";
    private static final String COLUMN_DATE_BOUGHT = "date_bought";
    private static final String COLUMN_CURRENCY = "currency";

    public SQLiteShare(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHARES_TABLE = "CREATE TABLE " + TABLE_SHARES + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_COMPANY + " TEXT,"
                + COLUMN_VALUE + " REAL,"
                + COLUMN_VALUE_PER_SHARE + " REAL,"
                + COLUMN_DATE_BOUGHT + " TEXT,"
                + COLUMN_CURRENCY + " TEXT"
                + ")";

        db.execSQL(CREATE_SHARES_TABLE);
    }

    public void updateShare(Share share) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, share.getUserId());
        values.put(COLUMN_NAME, share.getName());
        values.put(COLUMN_QUANTITY, Integer.toString(share.getQuantity()));
        values.put(COLUMN_COMPANY, share.getCompany());
        values.put(COLUMN_VALUE, Double.toString(share.getValue()));
        values.put(COLUMN_VALUE_PER_SHARE, Double.toString(share.getValuePerShare()));
        values.put(COLUMN_DATE_BOUGHT, share.getDateBought().toString());
        values.put(COLUMN_CURRENCY, share.getCurrency());

        db.update(TABLE_SHARES, values, COLUMN_ID + "=\'" + share.getId() + "\'", null);
        db.close();
    }

    public void updateOrInsertShare(Share share) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT COUNT(*) FROM " + TABLE_SHARES + " WHERE " + COLUMN_ID + " = \'" +
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
        values.put(COLUMN_USER_ID, share.getUserId());
        values.put(COLUMN_NAME, share.getName());
        values.put(COLUMN_QUANTITY, Integer.toString(share.getQuantity()));
        values.put(COLUMN_COMPANY, share.getCompany());
        values.put(COLUMN_VALUE, Double.toString(share.getValue()));
        values.put(COLUMN_VALUE_PER_SHARE, Double.toString(share.getValuePerShare()));
        values.put(COLUMN_DATE_BOUGHT, share.getDateBought().toString());
        values.put(COLUMN_CURRENCY, share.getCurrency());

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHARES);
        onCreate(db);
    }
}
