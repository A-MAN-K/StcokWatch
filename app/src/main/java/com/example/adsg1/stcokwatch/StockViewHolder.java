package com.example.adsg1.stcokwatch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by adsg1 on 3/15/2017.
 */

public class StockViewHolder extends RecyclerView.ViewHolder {


    public TextView symbol;
    public TextView companyName;
    public TextView arrow;
    public TextView price;
    public TextView priceChange;
    public TextView changePercent;



    public StockViewHolder(View itemView) {
        super(itemView);

        symbol = (TextView) itemView.findViewById(R.id.stcokSymbol);
        companyName = (TextView) itemView.findViewById(R.id.companyName);
        arrow = (TextView) itemView.findViewById(R.id.arrow);
        price = (TextView) itemView.findViewById(R.id.price);
        priceChange = (TextView) itemView.findViewById(R.id.priceChange);
        changePercent = (TextView) itemView.findViewById(R.id.changePC);


    }
}
