package com.example.tornstocks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tornstocks.Models.Stock;

public class AddEditTriggerActivity extends AppCompatActivity {
    private static final String TAG = "AddEditTriggerActivity";

    public static final int CREATE_TRIGGER = 1;
    public static final int CANCEL_TRIGGER = 2;
    public static final int EDIT_TRIGGER = 3;

    public static final String MODE = "com.example.tornstocks.MODE";
    public static final int EDIT_MODE = 0;
    public static final int CREATE_MODE = 1;

    public static final String EXTRA_STOCK = "com.example.tornstocks.STOCK";
    public static final String EXTRA_TRIGGER_PRICE = "com.example.tornstocks.TRIGGER_PRICE";

    private int mode;

    private EditText triggerPriceEt;
    private Button confirmButton;
    private TextView nameTv, acronymTv, currentPriceTv;
    private Stock stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trigger);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

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
                triggerPriceEt.setText(String.valueOf(intent.getFloatExtra(EXTRA_TRIGGER_PRICE,0)));
            } else if (mode == CREATE_MODE){
                setTitle("Create Trigger");
            }
            stock = intent.getParcelableExtra(EXTRA_STOCK);
            nameTv.setText(stock.getName());
            acronymTv.setText(stock.getAcronym());
            Log.d(TAG, "onCreate: Current price = " + stock.getCurrent_price());
            currentPriceTv.setText(String.valueOf(stock.getCurrent_price()));
        }


    }
}