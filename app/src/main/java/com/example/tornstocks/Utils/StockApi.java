package com.example.tornstocks.Utils;

import com.example.tornstocks.Responses.StockResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockApi {

    // Example of querying for stock data
    // https://api.torn.com/torn/?selections=stocks&key={API_KEY}
    @GET("/torn/?selections=stocks")
    Call<StockResponse> getStocks(
        @Query("key") String key
    );
}
