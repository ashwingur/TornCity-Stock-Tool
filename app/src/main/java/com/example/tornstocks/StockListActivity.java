package com.example.tornstocks;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tornstocks.Adapters.StockListAdapter;
import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Repositories.TriggerRepository;
import com.example.tornstocks.Service.TriggerCheckerService;
import com.example.tornstocks.Utils.Credentials;
import com.example.tornstocks.ViewModels.StockListViewModel;

import java.util.Collections;
import java.util.List;

public class StockListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int delay = 30000; // Delay between handler call in milliseconds
    final Handler handler = new Handler();

    private StockListViewModel stockListViewModel;

    private RecyclerView recyclerView;
    private StockListAdapter mAdapter;

    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        Log.d(TAG, "onCreate: Created");
        startService(new Intent(this, TriggerCheckerService.class));

        setTitle("Stocks");

        stockListViewModel = new ViewModelProvider(this).get(StockListViewModel.class);
        setupObservers();
        initLauncher();
        initRecycler();
        repeatStockQuery();
        initButton();
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
                            // Insert something here if required
                        } else {
                            Toast.makeText(StockListActivity.this, "Trigger not saved", Toast.LENGTH_SHORT).show();
                        }
                        TriggerRepository r = new TriggerRepository(getApplication());
                        Log.d(TAG, "onActivityResult: " + r.getAllTriggers().getValue());
                    }});
    }

    private void initButton(){
        Button b = findViewById(R.id.trigger_list_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockListActivity.this, TriggerListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_api_key:
                Toast.makeText(this, "Custom API has not been implemented yet", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about:
            Toast.makeText(this, "About has not been implemented yet", Toast.LENGTH_SHORT).show();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}