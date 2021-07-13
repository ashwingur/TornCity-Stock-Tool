package com.example.tornstocks.Requests;

import com.example.tornstocks.Utils.Credentials;
import com.example.tornstocks.Utils.StockApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockRetrofitBuilder {
    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Credentials.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();
    private static StockApi stockApi = retrofit.create(StockApi.class);

    public static StockApi getStockApi() { return stockApi; }
}
