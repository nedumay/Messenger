package com.example.messenger.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.ui.registration.RegistrationActivity;
import com.example.messenger.ui.reset.ResetPasswordActivity;
import com.example.messenger.ui.users.UsersActivity;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private TextView textViewForgotPassword;
    private TextView textViewRegistration;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginButton;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                mainViewModel.login(email, password);
            }
        });
        //launch intent to forgot password screen
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ResetPasswordActivity.newIntent(
                        MainActivity.this, editTextEmail.getText().toString().trim()
                );
                startActivity(intent);
            }
        });
        //launch intent to registration screen
        textViewRegistration.setOnClickListener(new View.OnClickListener() {
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

    private void initView() {
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewRegistration = findViewById(R.id.textViewRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

}