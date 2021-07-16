package com.example.tornstocks.ViewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Repositories.StockRepository;

import java.util.List;

public class StockListViewModel extends ViewModel {

    private StockRepository stockRepository;

    public StockListViewModel(){
        stockRepository = StockRepository.getInstance();
    }

    public LiveData<List<Stock>> getStocks(){ return stockRepository.getStocks(); }

    public void queryStocks(String key, Activity activity){ stockRepository.queryStocks(key, activity); }


}
