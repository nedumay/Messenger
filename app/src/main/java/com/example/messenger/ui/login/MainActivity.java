package com.example.messenger.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.databinding.ActivityMainBinding;
import com.example.messenger.ui.registration.RegistrationActivity;
import com.example.messenger.ui.reset.ResetPasswordActivity;
import com.example.messenger.ui.users.UsersActivity;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        binding.loginViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString().trim();
                String password = binding.editTextPassword.getText().toString().trim();
                mainViewModel.login(email, password);
            }
        });
        //launch intent to forgot password screen
        binding.textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ResetPasswordActivity.newIntent(
                        MainActivity.this, binding.editTextEmail.getText().toString().trim()
                );
                startActivity(intent);
            }
        });
        //launch intent to registration screen
        binding.registrationViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RegistrationActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        mainViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainViewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(MainActivity.this, firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

}