package com.example.adsg1.stcokwatch;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.R.drawable.ic_dialog_alert;
import static android.R.drawable.ic_menu_delete;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{


    private ArrayList<Stock> stockList = new ArrayList<Stock>();
    HashMap<String, Stock> hmap = new HashMap<String, Stock>();
    private ArrayList<Stock> temporaryList;
    private RecyclerView recyclerView;
    private StocksAdapter stocksAdapter;
    private Stock stock;
    private SwipeRefreshLayout swiper;
    private static final String TAG = "MainActivity";

    private static String stockURL = "http://www.marketwatch.com/investing/stock/";

    private static final int ADD_CODE = 1;
    private int UPDATE_CODE = 2;

    String stockToBeAdded;

    private String companyNameToStore;

    MainActivity mainActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkCheckOnStart(this);

        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        stocksAdapter = new StocksAdapter(stockList, this);
        recyclerView.setAdapter(stocksAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });



        // Creating the Database Instance
        DatabaseHandler.getInstance(this).setupDb();


    }




/*   Code added for Swife Refresh starts here  */
    private void doRefresh() {

        int result = 0;
        result = networkCheckOnSwipeRefresh(this);

        if( result != 1) {

            temporaryList = new ArrayList<Stock>();
            temporaryList.addAll(stockList);
            stockList.clear();

            for( Stock stock : temporaryList)
            {
                UPDATE_CODE = 9;
                hmap.put( stock.getSymbol(), stock);
                companyNameToStore = stock.getCompanyName();
                stockToBeAdded = stock.getSymbol();
                new AsyncDataLoader(mainActivity).execute(stock);

            }

            Collections.shuffle(stockList);
            stocksAdapter.notifyDataSetChanged();
          //  Toast.makeText(this, "List content shuffled", Toast.LENGTH_SHORT).show();
        }
        swiper.setRefreshing(false);


    }

/* Code added for Swipe Refresh ends here*/






/* Code added for DB starts here */
    @Override
    protected void onResume() {


        DatabaseHandler.getInstance(this).dumpLog();
            ArrayList<Stock> list = DatabaseHandler.getInstance(mainActivity).loadStocks();
        stockList.clear();
        stockList.addAll(list);
        Log.d(TAG, "onResume: " + list);
        Collections.sort(stockList, new SorterOnTheSymbol());
        stocksAdapter.notifyDataSetChanged();
        super.onResume();


    }



    @Override
    protected void onDestroy() {
        DatabaseHandler.getInstance(this).shutDown();
        super.onDestroy();
    }

/* Code added for DB ends here */






/* Code added for Menu starts here */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAdd:

                int result = networkCheckOnAddButton(this);
                if( result != 1) {

                    addStockDialogBoxCreator(this);

                }


            default:
                return super.onOptionsItemSelected(item);
        }
    }

/* Code added for Menu Bar ends here */






/* Code for Checking the Network Connection on the Start activity starts here */

public void networkCheckOnStart(Context context) {

    ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_LONG).show();
    } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setTitle("No Network Connection");
        builder.setMessage("You are not Connected to a Network");
        AlertDialog dialog = builder.create();
        dialog.show();


    }

}

/* Code for Checking the network Connection on the Start activity ends here */





/* Code for Checking the Network Connection on the AddButton activity starts here */

    public int networkCheckOnAddButton(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("No Network Connection");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("You cannnot add a new stock when not connected to the Network");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;

        }
        return result;
    }


/* Code for Checking the network Connection on the AddButton activity ends here */



/* Code for Checking the Network Connection on the Swipe Refresh activity starts here */

    public int networkCheckOnSwipeRefresh(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Toast.makeText(this, "You ARE Connected to the Internet!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("No Network Connection");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("You cannnot refresh the data when not connected to the Network");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;

        }
        return result;
    }


/* Code for Checking the network Connection on the Swipe Refresh activity ends here */




/* Code for Internet Async task 1 starts here*/

        /* Code added for creating the dialog box on the click of the ADD STOCK BUTTON starts here */

    public void addStockDialogBoxCreator(final Context context)
    {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    Intent data = getIntent();
    builder.setTitle("Stcok Selections");
    builder.setMessage("Please enter the Stock Symbol");
        final EditText addStock = new EditText(this);
        addStock.setInputType(InputType.TYPE_CLASS_TEXT);
        addStock.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        addStock.setGravity(Gravity.CENTER);
        builder.setView(addStock);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            int duplicateCheck = 0;
            stockToBeAdded = addStock.getText().toString();

            for( Stock stock : stockList)
            {
                if(stock.getSymbol().equals(stockToBeAdded))
                {
                    duplicateCheck =1;
                }
            }

            if( duplicateCheck == 1 )
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Intent data = getIntent();
                builder.setTitle("DUPLICATE STOCK");
                builder.setIcon(ic_dialog_alert);
                builder.setMessage("Stock Symbol "+stockToBeAdded+" is already displayed");
                AlertDialog dialogDuplicate = builder.create();
                dialogDuplicate.show();

            }
            else{

                new AsyncStockLoader(MainActivity.this).execute(stockToBeAdded);
            }


        }
    });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            dialog.dismiss();
        }
    });
    AlertDialog dialog = builder.create();
        dialog.show();

    }

    /* Code added for creating the dialog box on the click of the ADD STOCK BUTTON ends here */


  public void processDataAsyncTask1(final ArrayList<Stock> sList){


      if(sList.size() == 1 && sList.get(0).getSymbol().equals("123"))
      {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          Intent data = getIntent();
          builder.setTitle("SYMBOL NOT FOUND: "+stockToBeAdded);
          builder.setMessage("No Data for "+stockToBeAdded);
          AlertDialog dialog = builder.create();
          dialog.show();
      }
      else if(sList.size() == 1 )
      {
          companyNameToStore = sList.get(0).getCompanyName();
          new AsyncDataLoader(mainActivity).execute(sList.get(0));

      }
      else {

          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          Intent data = getIntent();
          builder.setTitle("Make a Selection");
          List<String> listSymbol = new ArrayList<String>();

          for (Stock stock : sList) {

              listSymbol.add(stock.getSymbol() + "-" + stock.getCompanyName());
          }

          final CharSequence[] items = listSymbol.toArray(new CharSequence[listSymbol.size()]);

          builder.setItems(items, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {

                  new AsyncDataLoader(mainActivity).execute(sList.get(which));
                  companyNameToStore = sList.get(which).getCompanyName();

              }
          });


          builder.setNegativeButton("NeverMind", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
              }
          });
          AlertDialog dialog = builder.create();
          dialog.show();
      }

  /*    stockList.addAll(sList);
      stocksAdapter.notifyDataSetChanged();
*/

    }


/*Code for Internet Async task 1 ends*/


/* Code for Internet Async Task 2 starts here*/

    public void processDataAsyncTask2( ArrayList<Stock> sList) {

        if( UPDATE_CODE == 9)
        {

           // Cursor cursor = DatabaseHandler.getInstance(this).getCompanyName(sList.get(0).getSymbol());
           // companyNameToStore =  cursor.getString(1);

           //   companyNameToStore = hmap.get(sList.get(0).getSymbol()).getCompanyName();
           //   sList.get(0).setCompanyName(companyNameToStore);
                DatabaseHandler.getInstance(this).updateStock(sList.get(0));
                stockList.addAll(sList);
                Collections.sort(stockList, new SorterOnTheSymbol());
                stocksAdapter.notifyDataSetChanged();

        }
        else {
            DatabaseHandler.getInstance(this).addStock(sList.get(0));
            stockList.addAll(sList);
            Collections.sort(stockList, new SorterOnTheSymbol());
            stocksAdapter.notifyDataSetChanged();
        }

        UPDATE_CODE = 2;


    }




/* Code for Internet Async Task 2 ends here*/




    @Override
    public void onClick(View v) {

        String symbolForURL= "";
        String URL;
        Log.d(TAG, "Running Good !!!");
        int pos = recyclerView.getChildLayoutPosition(v);
        symbolForURL = stockList.get(pos).getSymbol().toString();
        URL = stockURL+symbolForURL;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL));
        startActivity(i);

        Log.d(TAG, pos+"");
        Toast.makeText(this, "View has been clicked", Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "Working!!!!!");
        final int pos = recyclerView.getChildLayoutPosition(v);
        stock = stockList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setTitle("Delete Stock!");
        builder.setIcon(ic_menu_delete);
        builder.setMessage("Delete Stock"+"  "+ stock.getSymbol()+" ?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
/*
                stockList.remove(stock);
                stocksAdapter = new StocksAdapter(stockList, MainActivity.this );
                recyclerView.setAdapter(stocksAdapter);
*/
                DatabaseHandler.getInstance(mainActivity).deleteStock(stockList.get(pos).getSymbol());
                stockList.remove(pos);
                Collections.sort(stockList, new SorterOnTheSymbol());
                stocksAdapter.notifyDataSetChanged();


            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;

    }
}
