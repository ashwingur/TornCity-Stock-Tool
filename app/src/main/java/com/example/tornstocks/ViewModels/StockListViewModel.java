package com.example.tornstocks.ViewModels;

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

    public void queryStocks(String key){ stockRepository.queryStocks(key); }


}