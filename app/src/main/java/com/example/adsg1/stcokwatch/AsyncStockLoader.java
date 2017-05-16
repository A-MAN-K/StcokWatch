package com.example.adsg1.stcokwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
 * Created by adsg1 on 3/18/2017.
 */

public class AsyncStockLoader extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    private int count;

    private final String apiKEY = "53282099ab56a1f4498cd9fdc4abd6af62820bc4";
    //private final String dataURL = "https://restcountries.eu/rest/v1/all";

    private final String searchAppender ="&search_text=";
    private final String dataURL = "http://stocksearchapi.com/api/?api_key="+apiKEY+searchAppender;

    private static final String TAG = "AsyncStockLoader";



    public AsyncStockLoader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        mainActivity.processDataAsyncTask1(stockList);
    }


    @Override
    protected String doInBackground(String... params) {

        String completeURL = dataURL+params[0];
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
            //return null;

                sb.append("[{\"company_name\": \"NULL\", \"company_symbol\": \"123\", \"listing_exchange\": \"NULL\"}]");
                return sb.toString();
        }

        Log.d(TAG, "doInBackground: " + sb.toString());


        return sb.toString();
    }



    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                String companySymbol = jCountry.getString("company_symbol");

                String companyName = jCountry.getString("company_name");

                stockList.add(new Stock(companySymbol, companyName,1, 1, 1));

            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



}
