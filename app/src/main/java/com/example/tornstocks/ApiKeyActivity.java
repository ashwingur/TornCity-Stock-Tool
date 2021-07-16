package com.example.tornstocks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tornstocks.Repositories.ApiKeyRepository;

public class ApiKeyActivity extends AppCompatActivity {

    private EditText apiKeyEt;
    private Button saveApiKeyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_key);

        setTitle("Api Key");

        apiKeyEt = findViewById(R.id.api_key_et);
        saveApiKeyBtn = findViewById(R.id.save_api_button);

        apiKeyEt.setText(ApiKeyRepository.getApiKey(this));

        saveApiKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiKeyRepository.setApiKey(apiKeyEt.getText().toString(), ApiKeyActivity.this);
                Toast.makeText(ApiKeyActivity.this, "Api key saved", Toast.LENGTH_LONG).show();
            }
        });
    }
}