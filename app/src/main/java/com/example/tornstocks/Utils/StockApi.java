package com.example.tornstocks.Utils;

import com.example.tornstocks.Responses.StockResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StockApi {

    // Example of searching for stocks
    // https://api.torn.com/torn/?selections=stocks&key=0QvdYr3CeeLjimul
    @GET("/torn/?selections=stocks")
    Call<StockResponse> getStocks(
        @Query("key") String key
    );
}
