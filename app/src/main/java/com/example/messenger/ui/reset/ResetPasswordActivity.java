package com.example.messenger.ui.reset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.databinding.ActivityResetPasswordBinding;
import com.example.messenger.ui.login.MainActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String EXTRA_EMAIL = "email";

    private ActivityResetPasswordBinding binding;
    private ResetPasswordViewModel resetPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        binding.editTextEmail.setText(email);
        binding.buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString().trim();
                resetPasswordViewModel.resetPassword(email);
            }
        });

        binding.backToAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void observeViewModel() {
        resetPasswordViewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            "The reset link has been successfully sent",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        resetPasswordViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
}