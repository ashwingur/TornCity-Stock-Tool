package com.example.tornstocks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Requests.StockRetrofitBuilder;
import com.example.tornstocks.Responses.StockResponse;
import com.example.tornstocks.Utils.Credentials;
import com.example.tornstocks.ViewModels.StockListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class StockListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button button;

    private StockListViewModel stockListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        stockListViewModel = new ViewModelProvider(this).get(StockListViewModel.class);

//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try  {
//                    testResponse();
//                    //Your code goes here
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockListViewModel.queryStocks(Credentials.API_KEY);
                Toast.makeText(StockListActivity.this, stockListViewModel.getStocks().getValue() != null ? stockListViewModel.getStocks().getValue().toString() : "null", Toast.LENGTH_LONG).show();
            }
        });



    }


    public void setupObservers(){
        stockListViewModel.getStocks().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                Log.d(TAG, "onChanged: Observed a change");
                if (stocks != null){
                    Log.d(TAG, "onChanged: Stocks are: " + stocks);
                }
            }
        });
    }

//    public void testResponse(){
//        try {
//            Log.d(TAG, "testResponse: Running");
//            Response response = StockRetrofitBuilder.getStockApi().getStocks(Credentials.API_KEY).execute();
//            Log.d(TAG, "testResponse: Response code - " + response.code());
//            if (response.code() == 200){
//                StockResponse sr = (StockResponse)response.body();
//                Log.d(TAG, "testResponse: Stockresponse: " + sr);
//                List<Stock> list = new ArrayList<>(((StockResponse)response.body()).getStocks());
//                Log.d(TAG, "testResponse: Stocks: " + list);
//            }
//
//        } catch (IOException e) {
//            Log.d(TAG, "testResponse: Failed");
//            e.printStackTrace();
//        }
//    }
}