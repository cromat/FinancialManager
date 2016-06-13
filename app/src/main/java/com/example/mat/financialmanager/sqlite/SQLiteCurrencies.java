package com.example.mat.financialmanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Invoice;

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
public class SQLiteCurrencies extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "finmgr_currencies";

    // Ime tablice oglasa
    private static final String TABLE_CURRENCIES = "Currencies";

    private static final String COLUMN_BASE = "BASE";
    private static final String COLUM_HRK = Currencies.HRK.toString();
    private static final String COLUM_EUR = Currencies.EUR.toString();
    private static final String COLUM_USD = Currencies.USD.toString();

    String resultString = new String();

    public SQLiteCurrencies(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVOICE_TABLE = "CREATE TABLE " + TABLE_CURRENCIES + "("
                + COLUMN_BASE + " TEXT PRIMARY KEY,"
                + COLUM_HRK + " REAL,"
                + COLUM_EUR + " REAL,"
                + COLUM_USD + " REAL"
                + ")";
        db.execSQL(CREATE_INVOICE_TABLE);

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

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCIES);
        onCreate(db);
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

    public void updateTable(Context context){
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
