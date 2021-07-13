package com.example.tornstocks.Responses;

import com.example.tornstocks.Models.Stock;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockResponse {

    @SerializedName("stocks")
    private Map<String, Stock> stockMap;

    public List<Stock> getStocks(){ return new ArrayList<>(stockMap.values()); }

    @Override
    public String toString() {
        return "StockResponse{" +
                "stockMap=" + stockMap +
                '}';
    }
}
