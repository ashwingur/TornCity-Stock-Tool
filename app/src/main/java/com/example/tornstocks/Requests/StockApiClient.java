package com.example.tornstocks.Requests;

import android.app.Activity;
import android.content.Context;
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

    public void queryStocks(String key, Activity activity){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new RetriveStocksRunnable(key, activity));
    }

    private class RetriveStocksRunnable implements Runnable{
        private String key;
        private Activity activity;

        public RetriveStocksRunnable(String key, Activity activity){
            this.key = key;
            this.activity = activity;
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
                    if (sr.getError() != null && activity != null) {
                        Error error = sr.getError();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, String.format("[Torn Stock] Error %d: %s", error.getCode(), error.getWarning()), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (sr.getStockMap() != null) {
                            // Successful retrieval
                            List<Stock> list = new ArrayList<>(((StockResponse) response.body()).getStocks());
                            mStocks.postValue(list);
                        } else {
                            Log.d(TAG, "run: Stock Map was null");
                        }
                    }
                }

            } catch (IOException e) {
                Log.d(TAG, "testResponse: Failed");
                e.printStackTrace();
            }
        }
    }

}
