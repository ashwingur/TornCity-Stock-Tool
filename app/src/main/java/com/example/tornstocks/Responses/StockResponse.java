package com.example.tornstocks.Responses;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Requests.Error;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockResponse {

    @SerializedName("stocks")
    private Map<String, Stock> stockMap;

    private Error error;

    public List<Stock> getStocks(){ return new ArrayList<>(stockMap.values()); }
    public Map<String, Stock> getStockMap(){ return stockMap; }
    public Error getError() { return error; }

    @Override
    public String toString() {
        return "StockResponse{" +
                "stockMap=" + stockMap +
                '}';
    }
}
