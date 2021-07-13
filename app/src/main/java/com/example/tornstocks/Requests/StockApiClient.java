package com.example.tornstocks.Requests;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Responses.StockResponse;
import com.example.tornstocks.Utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class StockApiClient {
    private static final String TAG = "StockApiClient";

    // LiveData
    private MutableLiveData<List<Stock>> mStocks;

    // Singleton pattern
    private static StockApiClient instance;

    public static StockApiClient getInstance(){
        if (instance == null){
            instance =new StockApiClient();
        }
        return instance;
    }

    private StockApiClient(){ mStocks = new MutableLiveData<>(); }

    public LiveData<List<Stock>> getStocks(){ return mStocks; }

    public void queryStocks(String key){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new RetriveStocksRunnable(key));
    }

    private class RetriveStocksRunnable implements Runnable{
        private String key;

        public RetriveStocksRunnable(String key){
            this.key = key;
        }

        @Override
        public void run() {
            // Put query here
            try {
                Log.d(TAG, "run: Initiating Stock Retrival Query");
                Response response = StockRetrofitBuilder.getStockApi().getStocks(key).execute();
                Log.d(TAG, "run: Response code - " + response.code());
                if (response.code() == 200){
                    // Successful response
                    StockResponse sr = (StockResponse)response.body();
                    if (sr.getStockMap() != null){
                        // Successful retrieval
                        List<Stock> list = new ArrayList<>(((StockResponse)response.body()).getStocks());
                        Log.d(TAG, "testResponse: Stocks: " + list);
                        mStocks.postValue(list);
                    } else {
                        Log.d(TAG, "run: Stock Map was null");
                    }
                }

            } catch (IOException e) {
                Log.d(TAG, "testResponse: Failed");
                e.printStackTrace();
            }
        }
    }

}
