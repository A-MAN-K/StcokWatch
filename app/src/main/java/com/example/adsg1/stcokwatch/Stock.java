package com.example.adsg1.stcokwatch;

import android.support.annotation.NonNull;

/**
 * Created by adsg1 on 3/15/2017.
 */

public class Stock   {

    String symbol;
    String companyName;
    double price;
    double priceChange;
    double changePercent;



   private static int ctr =1;

   public Stock(){

       this.symbol = "Symobol " + ctr;
       this.companyName = System.currentTimeMillis()+"";
       this.price = 3.0+ ctr;
       this.priceChange = ctr;
       this.priceChange = ctr/100;
       ctr++;



   }

    public Stock(String symbol, String companyName, double price, double priceChange, double changePercent) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }



}
