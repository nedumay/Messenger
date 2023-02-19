package com.example.messenger.ui.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.ui.users.UsersActivity;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private CardView buttonSingUp;

    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                int age = Integer.parseInt(editTextAge.getText().toString().trim());
                registrationViewModel.singUp(email,password,name,lastName,age);
            }
        });

    }

    private void initView() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        buttonSingUp = findViewById(R.id.buttonSingUp);
    }

    private void observeViewModel(){
        registrationViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMassage) {
                if(errorMassage != null){
                    Toast.makeText(
                            RegistrationActivity.this,
                            errorMassage,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        registrationViewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Intent intent = UsersActivity.newIntent(
                            RegistrationActivity.this,
                            firebaseUser.getUid()
                    );
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @NonNull
    public static Intent newIntent(Context context){
        return new Intent(context, RegistrationActivity.class);
    }
}