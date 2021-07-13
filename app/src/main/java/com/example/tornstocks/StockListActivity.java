package com.example.tornstocks;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    public static final int delay = 15000; // Delay between handler call in milliseconds
    final Handler handler = new Handler();

    private StockListViewModel stockListViewModel;

    private RecyclerView recyclerView;
    private StockListAdapter mAdapter;

    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Stocks");

        stockListViewModel = new ViewModelProvider(this).get(StockListViewModel.class);
        setupObservers();
        initLauncher();
        initRecycler();
        repeatStockQuery();

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
                Intent intent = new Intent(StockListActivity.this, AddEditTriggerActivity.class);
                intent.putExtra(AddEditTriggerActivity.MODE, AddEditTriggerActivity.CREATE_MODE);
                intent.putExtra(AddEditTriggerActivity.EXTRA_STOCK, stock);
                launcher.launch(intent);
            }
        });
    }

    private void setupObservers(){
        stockListViewModel.getStocks().observe(this, new Observer<List<Stock>>() {
            @Override
            public void onChanged(List<Stock> stocks) {
                Collections.sort(stocks, Stock.StockPriceComparator);
                mAdapter.setStocks(stocks);
            }
        });
    }

    private void repeatStockQuery(){
        stockListViewModel.queryStocks(Credentials.API_KEY);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stockListViewModel.queryStocks(Credentials.API_KEY);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void initLauncher(){
        // Must come before initRecycler
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == AddEditTriggerActivity.CREATE_TRIGGER) {
                            Toast.makeText(StockListActivity.this, "Trigger created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StockListActivity.this, "Trigger not saved", Toast.LENGTH_SHORT).show();
                        }
                    }});
    }

}