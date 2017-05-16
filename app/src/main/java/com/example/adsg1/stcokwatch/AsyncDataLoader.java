package com.example.adsg1.stcokwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by adsg1 on 3/19/2017.
 */

public class AsyncDataLoader extends AsyncTask<Stock, Void, String> {



    private MainActivity mainActivity;
    private int count;

    Stock stock;

    private final String dataURL = "http://finance.google.com/finance/info?client=ig&q=";

    private static final String TAG = "AsyncDataLoader";

    public AsyncDataLoader(MainActivity ma) {
        mainActivity = ma;
    }



    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockDataList = parseJSON(s);
        mainActivity.processDataAsyncTask2(stockDataList);
    }


    @Override
    protected String doInBackground(Stock ... params) {

        stock = params[0];

        String completeURL = dataURL+stock.getSymbol();
        Uri dataUri = Uri.parse(completeURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {


            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        sb.delete(0,3);
        return sb.toString();
    }


    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String symbol = jCountry.getString("t");
                String price = jCountry.getString("l");
                String priceChange = jCountry.getString("c");
                String changePercent = jCountry.getString("cp");

                stockList.add(new Stock(symbol, stock.getCompanyName(), Double.parseDouble(price), Double.parseDouble(priceChange), Double.parseDouble(changePercent)));

            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }




}
