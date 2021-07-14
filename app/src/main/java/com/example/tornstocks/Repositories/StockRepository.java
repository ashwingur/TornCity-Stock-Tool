package com.example.tornstocks.Repositories;

import androidx.lifecycle.LiveData;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Requests.StockApiClient;

import java.util.List;

public class StockRepository {

    private static StockRepository instance;

    private StockApiClient stockApiClient;

    private String key;

    public static StockRepository getInstance(){
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

    public Stock getStockById(int stock_id){
        for (Stock s: getStocks().getValue()){
            if (s.getStock_id() == stock_id){
                return s;
            }
        }
        return null;
    }


}
