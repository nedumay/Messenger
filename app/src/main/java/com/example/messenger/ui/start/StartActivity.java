package com.example.messenger.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.databinding.ActivityStartBinding;
import com.example.messenger.ui.login.MainActivity;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.joinNowViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.newIntent(StartActivity.this);
                startActivity(intent);
                finish();
            }
        });

    }
}