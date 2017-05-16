package com.example.adsg1.stcokwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adsg1 on 3/15/2017.
 */

public class StocksAdapter extends RecyclerView.Adapter<StockViewHolder> {



    private static final String TAG = "EmployeesAdapter";
    private ArrayList<Stock> stockList;
    private MainActivity mainAct;

    public StocksAdapter(ArrayList<Stock> stockList, MainActivity mainActivity) {
        this.stockList = stockList;
        mainAct = mainActivity;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);


        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);


        return new StockViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {

        Stock stock = stockList.get(position);
        if(stock.getPriceChange() < 0) {
            holder.symbol.setTextColor(Color.parseColor("#FF0000"));
            holder.symbol.setText(stock.getSymbol());
        }
        else{
            holder.symbol.setTextColor(Color.parseColor("#008000"));
            holder.symbol.setText(stock.getSymbol());
        }
        if(stock.getPriceChange() < 0) {
            holder.companyName.setTextColor(Color.parseColor("#FF0000"));
            holder.companyName.setText(stock.getCompanyName());
        }
        else{
            holder.companyName.setTextColor(Color.parseColor("#008000"));
            holder.companyName.setText(stock.getCompanyName());
        }
        if(stock.getPriceChange() < 0)
        {
            holder.arrow.setTextColor(Color.parseColor("#FF0000"));
            holder.arrow.setText("▼");
        }
        else
        {
            holder.arrow.setTextColor(Color.parseColor("#008000"));
            holder.arrow.setText("▲");
        }
        if(stock.getPriceChange() < 0) {
            holder.price.setTextColor(Color.parseColor("#FF0000"));
            holder.price.setText(String.valueOf(stock.getPrice()));
        }
        else{
            holder.price.setTextColor(Color.parseColor("#008000"));
            holder.price.setText(String.valueOf(stock.getPrice()));
        }
        if(stock.getPriceChange() < 0) {
            holder.priceChange.setTextColor(Color.parseColor("#FF0000"));
            holder.priceChange.setText(String.valueOf(stock.getPriceChange()));
        }
        else{
            holder.priceChange.setTextColor(Color.parseColor("#008000"));
            holder.priceChange.setText(String.valueOf(stock.getPriceChange()));
        }
        if(stock.getPriceChange() < 0) {
            holder.changePercent.setTextColor(Color.parseColor("#FF0000"));
            holder.changePercent.setText("( " + String.valueOf(stock.getChangePercent()) + ")");
        }
        else{
            holder.changePercent.setTextColor(Color.parseColor("#008000"));
            holder.changePercent.setText("( " + String.valueOf(stock.getChangePercent()) + ")");
        }

    }

    @Override
    public int getItemCount() {

        return stockList.size();

    }
}
