package com.example.tornstocks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.Repositories.TriggerRepository;
import com.example.tornstocks.ViewModels.StockListViewModel;
import com.example.tornstocks.ViewModels.TriggerListViewModel;

import java.util.List;

public class TriggerListActivity extends AppCompatActivity {
    private static final String TAG = "TriggerListActivity";

    private TriggerListViewModel triggerListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_list);

        triggerListViewModel = new ViewModelProvider(this).get(TriggerListViewModel.class);

        Button btn = findViewById(R.id.button_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + triggerListViewModel.getTriggers().getValue());
            }
        });

        setUpObservers();
    }

    private void setUpObservers() {
        triggerListViewModel.getTriggers().observe(this, new Observer<List<Trigger>>() {
            @Override
            public void onChanged(List<Trigger> triggers) {
                Log.d(TAG, "onChanged: " + triggers);
            }
        });
    }
}