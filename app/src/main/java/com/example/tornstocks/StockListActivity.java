package com.example.tornstocks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tornstocks.Adapters.StockListAdapter;
import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Requests.StockRetrofitBuilder;
import com.example.tornstocks.Responses.StockResponse;
import com.example.tornstocks.Utils.Credentials;
import com.example.tornstocks.ViewModels.StockListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class StockListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private StockListViewModel stockListViewModel;

    private RecyclerView recyclerView;
    private StockListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Stocks");

        stockListViewModel = new ViewModelProvider(this).get(StockListViewModel.class);
        setupObservers();

        initRecycler();
        stockListViewModel.queryStocks(Credentials.API_KEY);

    }


    private void initRecycler(){
        recyclerView = findViewById(R.id.recycler_view_stocks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new StockListAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnStockClickListener(new StockListAdapter.OnStockClickListener() {
            @Override
            public void OnStockClick(Stock stock) {
                Toast.makeText(StockListActivity.this, "Clicked on " + stock.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupObservers(){
        stockListViewModel.getStocks().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                Collections.sort(stocks, Stock.StockPriceComparator);
                mAdapter.setStocks(stocks);
                Log.d(TAG, "onChanged: Observed a change");
                if (stocks != null){
                    Log.d(TAG, "onChanged: Stocks are: " + stocks);
                }
            }
        });
    }

}