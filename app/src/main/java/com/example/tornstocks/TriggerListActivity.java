package com.example.tornstocks;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tornstocks.Adapters.TriggerListAdapter;
import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.Repositories.StockRepository;
import com.example.tornstocks.Repositories.TriggerRepository;
import com.example.tornstocks.ViewModels.TriggerListViewModel;

import java.util.List;

public class TriggerListActivity extends AppCompatActivity {
    private static final String TAG = "TriggerListActivity";

    private TriggerListViewModel triggerListViewModel;

    private RecyclerView recyclerView;
    private TriggerListAdapter mAdapter;

    private ActivityResultLauncher<Intent> launcher;

    private DialogInterface.OnClickListener dialogClickListener;
    private int delete_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_list);

        triggerListViewModel = new ViewModelProvider(this).get(TriggerListViewModel.class);

        setTitle("Stock Triggers");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        initLauncher();
        initDialogClickListener();
        initRecycler();
        setUpObservers();
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.recycler_view_triggers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new TriggerListAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnTriggerListener(new TriggerListAdapter.OnTriggerListener() {
            @Override
            public void OnTriggerClick(Trigger trigger) {
                Intent intent = new Intent(TriggerListActivity.this, AddEditTriggerActivity.class);
                intent.putExtra(AddEditTriggerActivity.MODE, AddEditTriggerActivity.EDIT_MODE);
                intent.putExtra(AddEditTriggerActivity.EXTRA_STOCK,
                        StockRepository.getInstance().getStockById(trigger.getStock_id()));
                intent.putExtra(AddEditTriggerActivity.EXTRA_TRIGGER, trigger);
                launcher.launch(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete_index = viewHolder.getAdapterPosition();
                Trigger t = mAdapter.getTrigger(delete_index);
                AlertDialog.Builder builder = new AlertDialog.Builder(TriggerListActivity.this);
                builder.setMessage(String.format("Delete %s trigger for %.2f?", t.getAcronym(), t.getTrigger_price()))
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpObservers() {
        triggerListViewModel.getTriggers().observe(this, new Observer<List<Trigger>>() {
            @Override
            public void onChanged(List<Trigger> triggers) {
                mAdapter.setTriggers(triggers);
                Log.d(TAG, "onChanged: " + triggers);
            }
        });
    }

    private void initLauncher(){
        // Must come before initRecycler
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == AddEditTriggerActivity.EDIT_TRIGGER) {
                            // Insert something here if required
                        } else {
                            Toast.makeText(TriggerListActivity.this, "Trigger not saved", Toast.LENGTH_SHORT).show();
                        }
                        TriggerRepository r = new TriggerRepository(getApplication());
                        Log.d(TAG, "onActivityResult: " + r.getAllTriggers().getValue());
                    }});
    }

    private void initDialogClickListener(){
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        triggerListViewModel.delete(mAdapter.getTrigger(delete_index));
                        Toast.makeText(TriggerListActivity.this, "Trigger deleted", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyItemRemoved(delete_index);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        mAdapter.notifyItemChanged(delete_index);
                        break;
                }
            }
        };
    }

}