package com.example.tornstocks;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tornstocks.Models.Stock;
import com.example.tornstocks.Models.Trigger;
import com.example.tornstocks.Repositories.TriggerRepository;

public class AddEditTriggerActivity extends AppCompatActivity {
    private static final String TAG = "AddEditTriggerActivity";

    public static final int CREATE_TRIGGER = 1;
    public static final int CANCEL_TRIGGER = 2;
    public static final int EDIT_TRIGGER = 3;

    public static final String MODE = "com.example.tornstocks.MODE";
    public static final int EDIT_MODE = 0;
    public static final int CREATE_MODE = 1;

    public static final String EXTRA_STOCK = "com.example.tornstocks.STOCK";
    public static final String EXTRA_TRIGGER = "com.example.tornstocks.TRIGGER";

    private int mode;

    private EditText triggerPriceEt;
    private Button confirmButton;
    private TextView nameTv, acronymTv, currentPriceTv;
    private Stock stock;
    private Trigger trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trigger);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        triggerPriceEt = findViewById(R.id.add_edit_trigger);
        confirmButton = findViewById(R.id.add_edit_button);
        nameTv = findViewById(R.id.add_edit_name);
        acronymTv = findViewById(R.id.add_edit_acronym);
        currentPriceTv = findViewById(R.id.add_edit_current_price);

        triggerPriceEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        Intent intent = getIntent();

        if (intent.hasExtra(MODE)){
            mode = intent.getIntExtra(MODE, -1);
            if (mode == EDIT_MODE){
                setTitle("Edit Trigger");
                trigger = intent.getParcelableExtra(EXTRA_TRIGGER);
                triggerPriceEt.setText(String.valueOf(trigger.getTrigger_price()));
            } else if (mode == CREATE_MODE){
                setTitle("Create Trigger");
            }
            stock = intent.getParcelableExtra(EXTRA_STOCK);
            nameTv.setText(stock.getName());
            acronymTv.setText(stock.getAcronym());
            Log.d(TAG, "onCreate: Current price = " + stock.getCurrent_price());
            currentPriceTv.setText(String.valueOf(stock.getCurrent_price()));
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });


    }

    public void onButtonClick() {
        if (triggerPriceEt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter a trigger value", Toast.LENGTH_SHORT).show();
            return;
        }
        TriggerRepository repository = new TriggerRepository(getApplication());
        if (mode == EDIT_MODE) {
            setResult(EDIT_TRIGGER);
            trigger.setTrigger_price(Float.valueOf(triggerPriceEt.getText().toString()));
            trigger.setCreation_price(stock.getCurrent_price());
            repository.update(trigger);
            Toast.makeText(this, String.format("%s trigger updated to %.2f", trigger.getAcronym(), trigger.getTrigger_price()), Toast.LENGTH_SHORT).show();
        } else if (mode == CREATE_MODE) {
            setResult(CREATE_TRIGGER);
            trigger = new Trigger(stock.getStock_id(), stock.getAcronym(),
                    Float.parseFloat(triggerPriceEt.getText().toString()), stock.getCurrent_price());
            repository.insert(trigger);
            Toast.makeText(this, String.format("%s trigger created at %.2f", trigger.getAcronym(), trigger.getTrigger_price()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error in trigger operation", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent data = new Intent();
                setResult(CANCEL_TRIGGER, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}