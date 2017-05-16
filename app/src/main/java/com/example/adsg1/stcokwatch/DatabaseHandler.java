
package com.example.adsg1.stcokwatch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "StockAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockWatchTable";
    ///DB Columns
    private static final String symbol = "SYMBOL";
    private static final String companyName = "COMPANY_NAME";
    private static final String price = "PRICE";
    private static final String priceChange = "PRICE_CHANGE";
    private static final String changePercent = "CHANGE_PERCENT";



    // Columns to add later
    private static final String AREA = "Area";
    private static final String NUMBORDERS = "NumBorders";

    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    symbol + " TEXT not null unique," +
                    companyName + " TEXT not null, " +
                    price + " TEXT not null, " +
                    priceChange + " TEXT not null, " +
                    changePercent + " INT not null)";

    private SQLiteDatabase database;

    private static final String DATABASE_ALTER_TABLE_FOR_V2 = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + AREA + " int not null default 0;";

    private static final String DATABASE_ALTER_TABLE_FOR_V3 = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + NUMBORDERS + " int not null default 0;";


    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context);
        return instance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Mking New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void setupDb() {
        database = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 2) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V2);
        }
        if (newVersion == 3) {
            db.execSQL(DATABASE_ALTER_TABLE_FOR_V3);
        }

    }

    public ArrayList<Stock> loadStocks() {

        Log.d(TAG, "loadCountries: LOADING COUNTRY DATA FROM DB");
        ArrayList<Stock > stocks = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_NAME,  // The table to query
                new String[]{symbol, companyName, price, priceChange, changePercent}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String companyName = cursor.getString(1);
                String price = cursor.getString(2);
                String priceChange = cursor.getString(3);
                String changePercent = cursor.getString(4);
                stocks.add(new Stock(symbol, companyName, Double.parseDouble(price), Double.parseDouble(priceChange), Double.parseDouble(changePercent)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadCountries: DONE LOADING COUNTRY DATA FROM DB");

        return stocks;
    }


    public Cursor getCompanyName(String symbolFromMain ){
        Cursor cur = database.query(TABLE_NAME, null,
                "symbol= ?", new String[] { "" + symbolFromMain }, null,
                null, null);
        if (cur != null) {
            cur.moveToFirst();
        }
        return cur;
    }


    public void addStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(symbol, stock.getSymbol());
        values.put(companyName, stock.getCompanyName());
        values.put(price, stock.getPrice());
        values.put(priceChange, stock.getPriceChange());
        values.put(changePercent, stock.getChangePercent());

        deleteStock(stock.getSymbol());
        long key = database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addCountry: " + key);
    }

    public void updateStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(symbol, stock.getSymbol());
        values.put(companyName, stock.getCompanyName());
        values.put(price, stock.getPrice());
        values.put(priceChange, stock.getPriceChange());
        values.put(changePercent, stock.getChangePercent());

        long key = database.update(
                TABLE_NAME, values, symbol + " = ?", new String[]{stock.getSymbol()});

        Log.d(TAG, "updateStock: " + key);
    }

    public void deleteStock(String name) {
        Log.d(TAG, "deleteStock: " + name);
        int cnt = database.delete(TABLE_NAME, symbol + " = ?", new String[]{name});
        Log.d(TAG, "deleteStock: " + cnt);
    }

    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String country = cursor.getString(0);
                String region = cursor.getString(1);
                String subRegion = cursor.getString(2);
                String capital = cursor.getString(3);
                int population = cursor.getInt(4);
                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", symbol + ":", country) +
                        String.format("%s %-18s", companyName + ":", region) +
                        String.format("%s %-18s", price + ":", subRegion) +
                        String.format("%s %-18s", priceChange + ":", capital) +
                        String.format("%s %-18s", changePercent + ":", population));
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public void shutDown() {
        database.close();
    }
}
