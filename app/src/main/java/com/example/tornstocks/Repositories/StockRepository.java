package com.example.tornstocks.Repositories;

import androidx.lifecycle.LiveData;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Requests.StockApiClient;

import java.util.List;

public class StockRepository {

    private static StockRepository instance;

    private StockApiClient stockApiClient;

    private String key;

    private StockRepository getInstance(){
        if (instance == null){
            instance = new StockRepository();
        }
        return instance;
    }

    private StockRepository(){
        stockApiClient = StockApiClient.getInstance();
    }

    public LiveData<List<Stock>> getStocks(){ return stockApiClient.getStocks(); }

    public void queryStocks(String key){
        stockApiClient.queryStocks(key);
    }


}
