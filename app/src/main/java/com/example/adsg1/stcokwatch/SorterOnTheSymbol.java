package com.example.adsg1.stcokwatch;

import java.util.Comparator;

/**
 * Created by adsg1 on 3/19/2017.
 */

public class SorterOnTheSymbol implements Comparator<Stock> {
    @Override
    public int compare(Stock stock1, Stock stock2) {
            return stock1.getSymbol().compareTo(stock2.getSymbol()  );
    }
}
