package com.example.adsg1.stcokwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by adsg1 on 3/17/2017.
 */

public class StockDetails extends AppCompatActivity {

    private EditText symbol;
    private EditText companyName;
    private EditText price;
    private EditText priceChange;
    private EditText changePercent;
    private Stock currentStock;
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_details);

 //       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        symbol = (EditText) findViewById(R.id.symbol);
        companyName = (EditText) findViewById(R.id.companyName);
        price = (EditText) findViewById(R.id.price);
        priceChange = (EditText) findViewById(R.id.priceChange);
        changePercent = (EditText) findViewById(R.id.changePercent);

        // Check to see if a Country object was provided in the activity's intent
        // Set up the textviews if so.
        Intent intent = getIntent();
        if (intent.hasExtra("COUNTRY")) {
            currentStock = (Stock) intent.getSerializableExtra("COUNTRY");
            symbol.setText(currentStock.getSymbol());
            symbol.setFocusable(false);
            companyName.setText(currentStock.getCompanyName());
            price.setText(String.valueOf(currentStock.getPrice()));
            priceChange.setText(String.valueOf(currentStock.getPriceChange()));
            changePercent.setText(String.format("%d", currentStock.getChangePercent()));
            isAdd = false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doSave(View v) {

        String symbolData = symbol.getText().toString();
        String companyNameData = companyName.getText().toString();
        double priceData = Double.parseDouble(price.getText().toString());
        double priceChangeData = Double.parseDouble(priceChange.getText().toString());
        double changePercentData = Double.parseDouble(changePercent.getText().toString());

        Stock stock = new Stock(symbolData, companyNameData,  priceData, priceChangeData, changePercentData);
        if (isAdd)
            DatabaseHandler.getInstance(this).addStock(stock);
        else
            DatabaseHandler.getInstance(this).updateStock(stock);
        finish();
    }


}
