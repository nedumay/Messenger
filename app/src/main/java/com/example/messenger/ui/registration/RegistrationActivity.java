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
import com.example.messenger.databinding.ActivityRegistrationBinding;
import com.example.messenger.ui.login.MainActivity;
import com.example.messenger.ui.users.UsersActivity;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
       binding.buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString().trim();
                String password = binding.editTextPassword.getText().toString().trim();
                String name = binding.editTextName.getText().toString().trim();
                String lastName = binding.editTextLastName.getText().toString().trim();
                int age = Integer.parseInt(binding.editTextAge.getText().toString().trim());
                registrationViewModel.singUp(email,password,name,lastName,age);
            }
        });
       binding.backToAccountLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = MainActivity.newIntent(RegistrationActivity.this);
               startActivity(intent);
               finish();
           }
       });

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